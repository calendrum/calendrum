#!/usr/bin/env bash
# Fuzzing campaign: every target (or the ones given) for the same time, one after another, on a quiet machine.
#
#   fuzz/campaign.sh                      # every target, 20 minutes each
#   fuzz/campaign.sh --duration 5m Lexer StreamingDifferential Interceptor
#
# Before each target it waits until the Mac runs on AC power (`pmset`), so that a campaign left for the night does not
# fuzz on battery, and the whole run stays awake under `caffeinate -dimsu`. Logs, a summary with the final libFuzzer line
# of each target and the list of new findings go to fuzz/build/campaign/<UTC time>/. Findings (crash-*, timeout-*,
# oom-*) are the files Jazzer writes into the input directory of the target; the summary lists those that appeared
# during the run, and they stay there for the regression run.
set -euo pipefail

cd "$(dirname "$0")/.."
duration=20m
targets=()
while [ $# -gt 0 ]; do
  case "$1" in
    --duration) duration="$2"; shift 2 ;;
    -h|--help) sed -n '2,12p' "$0"; exit 0 ;;
    *) targets+=("$1"); shift ;;
  esac
done
if [ ${#targets[@]} -eq 0 ]; then
  # The list of fuzz/build.gradle.kts, in its order.
  targets=($(sed -n '/^val fuzzTargets = listOf(/,/^)/p' fuzz/build.gradle.kts | grep -o '"[A-Za-z0-9]*"' | tr -d '"'))
fi

out="fuzz/build/campaign/$(date -u +%Y%m%dT%H%M%SZ)"
mkdir -p "$out"
inputs=fuzz/src/test/resources/io/github/calendrum/fuzz
summary="$out/summary.txt"
echo "campaign: ${#targets[@]} targets x $duration, started $(date -u +%FT%TZ)" | tee "$summary"

# Compiles once, so the first target does not spend its time on the build.
./gradlew :fuzz:testClasses --console=plain -q

on_ac() { pmset -g batt 2>/dev/null | head -1 | grep -q "AC Power"; }

for target in "${targets[@]}"; do
  until on_ac; do
    echo "$(date -u +%T) $target: waiting for AC power" | tee -a "$summary"
    sleep 60
  done
  before="$out/.before-$target"
  find "$inputs/${target}FuzzTestInputs" -type f \( -name 'crash-*' -o -name 'timeout-*' -o -name 'oom-*' \) \
    2>/dev/null | sort > "$before" || true
  start=$(date -u +%T)
  status=0
  caffeinate -dimsu ./gradlew ":fuzz:fuzz$target" -Pfuzz.duration="$duration" --console=plain \
    > "$out/$target.log" 2>&1 || status=$?
  done_line=$(grep -E '^#[0-9]+[[:space:]]+DONE' "$out/$target.log" | tail -1 || true)
  new=$(comm -13 "$before" <(find "$inputs/${target}FuzzTestInputs" -type f \
    \( -name 'crash-*' -o -name 'timeout-*' -o -name 'oom-*' \) 2>/dev/null | sort) || true)
  rm -f "$before"
  power=$(on_ac && echo AC || echo battery)
  echo "$start-$(date -u +%T) $target exit=$status power=$power ${done_line:-no DONE line}" | tee -a "$summary"
  if [ -n "$new" ]; then
    echo "$new" | sed 's/^/  finding: /' | tee -a "$summary"
  fi
done
echo "campaign done $(date -u +%FT%TZ)" | tee -a "$summary"
