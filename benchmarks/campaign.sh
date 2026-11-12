#!/usr/bin/env bash
# Benchmark campaign: the whole performance report in one command per night, on a quiet machine.
#
#   benchmarks/campaign.sh jvm                 # JMH `full` profile, every suite, grouped (about 7.5 hours)
#   benchmarks/campaign.sh native-web          # cold start, Native `full`, JS and Wasm `quick`, re-measured pairs
#   benchmarks/campaign.sh web                 # only the JS and Wasm part of native-web, with the re-measured pairs
#   benchmarks/campaign.sh native-web --dry-run                # print the steps and the time estimate, run nothing
#   benchmarks/campaign.sh native-web --only wasm-tz rr-wasm-tz-1   # repeat chosen steps (labels from --dry-run)
#   benchmarks/campaign.sh jvm --skip jvm-cursor-charset       # everything but the given steps
#   benchmarks/campaign.sh jvm --smoke         # JMH `-wi 0 -i 1 -r 100ms -f 1` on every pair, no stand checks
#   benchmarks/campaign.sh native-web --smoke  # every step on the quick profile and one small corpus
#
# Before every step the script checks the stand: the Mac is on AC power, none of Chrome, Safari, Telegram, Xcode,
# Android Studio, IntelliJ IDEA, VLC, Spotify or Zoom runs (by the path of the executable, not by arguments), and at most
# one Gradle daemon is alive. Otherwise it writes WAIT to conditions.log and sleeps 60 s; after 4 hours of waiting it
# gives up. A background monitor looks at the power every 10 s; a step during which the Mac was on battery, or asleep, is
# spoiled and runs again (up to two attempts). The whole run stays awake under `caffeinate -dimsu`.
#
# After every step the reports of that step (the run directory of the profile and summary.md) are copied to
# benchmarks/build/campaign/<UTC time>/<label>/ and a line goes to summary.txt next to conditions.log and the Gradle logs.
# The Gradle build directory is reused by the next step, so only that copy survives. Run nothing else during a campaign.
set -euo pipefail

self="$(cd "$(dirname "$0")" && pwd)/$(basename "$0")"
cd "$(dirname "$self")/.."

# Seconds per (benchmark, parameter) pair, calibrated on the first full run (252 JVM pairs in 6 h 46 min, 54 quick runs in
# 1 h 25 min) and the smoke run (a step costs 10 to 30 s of Gradle on top of its pairs).
# JVM full: 3 forks x (5 x 2 s + 10 x 2 s) plus fork start; Native full: 5 x 2 s + 30 x 2 s plus the corpus; JS and Wasm
# quick: 3 x 1 s + 5 x 1 s plus the corpus.
JVM_PAIR_S=${CAMPAIGN_JVM_PAIR_S:-97}
NATIVE_PAIR_S=${CAMPAIGN_NATIVE_PAIR_S:-80}
WEB_PAIR_S=${CAMPAIGN_WEB_PAIR_S:-11}
GRADLE_STEP_S=${CAMPAIGN_GRADLE_STEP_S:-30}
NIGHT_LIMIT_S=$((8 * 3600))
WAIT_LIMIT_S=$((4 * 3600))

orig_args=("$@")
set_name=""
dry_run=0
smoke=0
only=()
skip=()
while [ $# -gt 0 ]; do
  case "$1" in
    jvm|native-web|web) set_name="$1"; shift ;;
    --dry-run) dry_run=1; shift ;;
    --smoke) smoke=1; shift ;;
    --only) shift; while [ $# -gt 0 ] && [ "${1#--}" = "$1" ]; do only+=("$1"); shift; done ;;
    --skip) shift; while [ $# -gt 0 ] && [ "${1#--}" = "$1" ]; do skip+=("$1"); shift; done ;;
    -h|--help) sed -n '2,23p' "$0"; exit 0 ;;
    *) echo "unknown argument: $1 (see --help)" >&2; exit 2 ;;
  esac
done
if [ -z "$set_name" ]; then
  echo "usage: benchmarks/campaign.sh <jvm|native-web|web> [--dry-run] [--smoke] [--only <label>...] [--skip <label>...]" >&2
  exit 2
fi

# The caffeinate wrapper holds the Mac awake for the whole campaign, including the waits between steps.
if [ "$dry_run" -eq 0 ] && [ -z "${CALENDRUM_CAMPAIGN_CAFFEINATED:-}" ]; then
  export CALENDRUM_CAMPAIGN_CAFFEINATED=1
  exec caffeinate -dimsu "$self" "${orig_args[@]}"
fi

SRC=benchmarks/src
PKG=io/github/calendrum/benchmarks
REPORTS=benchmarks/build/reports/benchmarks

# ---------------------------------------------------------------------------------------------------------------------
# Suites. Names are the packages under benchmarks/src/*/kotlin/io/github/calendrum/benchmarks/. `sanity` (pipeline check),
# `corpus` (generator), `coldStart` (own task) and the fuzz seeds are not measured; a new package has to be added here.

# JVM groups: label, suites. Every group is at most about 50 minutes (JMH writes its JSON only at the end of the run and
# `-foe true` stops a run on the first error); the first fourteen are the groups of the first full run, the last one holds
# the suites that were measured only with `quick` before.
JVM_GROUPS="
jvm-parseTyped:parseTyped|baselineReadAll
jvm-selective:selective
jvm-schema-patch:schema|patch
jvm-rrule:rrule|baselineRrule
jvm-tz:tz
jvm-occurrences:occurrences
jvm-freebusy:freebusy
jvm-validate-fixups-feedPublisher:validate|fixups|feedPublisher
jvm-adapters:adapters
jvm-reader:reader|corpusScan
jvm-parse:parse|baselineParse
jvm-write:write|writer|baselineWrite
jvm-stream:stream
jvm-lexerTree:lexerTree
jvm-fingerprints-values:fingerprints|values
jvm-cursor-charset:cursor|charset
"

# Native groups: one or two suites, 15 to 32 pairs (about 45 minutes at most). `huge` stays: Native has the memory.
NATIVE_GROUPS="
native-adapters:adapters
native-reader:reader|corpusScan
native-lexerTree:lexerTree
native-stream:stream
native-parse-write:parse|write
native-cursor-fingerprints:cursor|fingerprints
native-parseTyped-selective:parseTyped|selective
native-writer-charset:writer|charset
native-schema-patch:schema|patch
native-fixups-feedPublisher:fixups|feedPublisher
native-validate-values:validate|values
native-rrule-tz-occurrences:rrule|tz|occurrences
native-freebusy:freebusy
"

# JS and Wasm: one suite per step, without `huge` (the tree of a 100 MB feed does not fit the Node.js heap).
WEB_SUITES="reader corpusScan lexerTree stream parse write cursor fingerprints parseTyped selective writer charset schema
patch fixups feedPublisher validate values adapters rrule tz occurrences freebusy"

# The JVM groups whose code changed after the last whole `jvm` night: `native-web` measures them again, on the same stand, so
# that the report describes one version of the code without a second JVM night. Empty after a whole `jvm` night of the release.
JVM_CHANGED="jvm-occurrences jvm-freebusy"

# ---------------------------------------------------------------------------------------------------------------------
# Counting pairs from the sources: @Benchmark methods times the values of the @Param lines of the class.

suite_files() { # suite platform
  ls "$SRC"/commonMain/kotlin/$PKG/"$1"/*.kt 2>/dev/null || true
  if [ "$2" = jvm ]; then ls "$SRC"/jvmMain/kotlin/$PKG/"$1"/*.kt 2>/dev/null || true; fi
}

suite_pairs() { # suite platform(jvm|native|web)
  local files nohuge=0
  files=$(suite_files "$1" "$2")
  [ "$2" = web ] && nohuge=1
  if [ -z "$files" ]; then echo 0; return; fi
  # shellcheck disable=SC2086
  awk -v nohuge="$nohuge" '
    function flush() { if (methods > 0) total += methods * (prod ? prod : 1); methods = 0; prod = 0 }
    FNR == 1 { flush() }
    /^[[:space:]]*@Benchmark[[:space:]]*$/ { methods++ }
    /@Param\(/ {
      line = $0; n = gsub(/"[^"]*"/, "&", line)
      if (nohuge && line ~ /"huge"/) n--
      if (n == 0) n = 1
      prod = (prod ? prod : 1) * n
    }
    END { flush(); print total + 0 }
  ' $files
}

group_pairs() { # "a|b|c" platform
  local total=0 s
  for s in $(echo "$1" | tr '|' ' '); do total=$((total + $(suite_pairs "$s" "$2"))); done
  echo "$total"
}

corpus_values() { # suite nohuge(0|1): the values of `@Param ... var corpus`, comma-separated; empty if there is none
  local files
  files=$(ls "$SRC"/commonMain/kotlin/$PKG/"$1"/*.kt 2>/dev/null || true)
  [ -z "$files" ] && return 0
  # shellcheck disable=SC2086
  awk -v nohuge="$2" '
    /@Param\(/ {
      vals = ""; line = $0
      while (match(line, /"[^"]*"/)) {
        v = substr(line, RSTART + 1, RLENGTH - 2)
        if (!(nohuge && v == "huge")) vals = vals (vals == "" ? "" : ",") v
        line = substr(line, RSTART + RLENGTH)
      }
      pending = 1; next
    }
    pending && /corpus/ { print vals; exit }
    pending && NF { pending = 0 }
  ' $files
}

smallest_corpus() { # suite: invite, else personal, else the first value; empty without a corpus parameter
  local list
  list=$(corpus_values "$1" 0)
  [ -z "$list" ] && return 0
  case ",$list," in
    *,invite,*) echo invite ;;
    *,personal,*) echo personal ;;
    *) echo "${list%%,*}" ;;
  esac
}

include_arg() { printf -- '-Pbench.include=io\\.github\\.calendrum\\.benchmarks\\.(%s)\\.' "$1"; }

# ---------------------------------------------------------------------------------------------------------------------
# Steps: parallel arrays (bash 3.2 has no associative ones). The command is the Gradle command line, %q-quoted.

S_LABEL=(); S_KIND=(); S_PROFILE=(); S_EST=(); S_PAIRS=(); S_CMD=()

add_step() { # label kind profile estimate-seconds pairs -- gradle arguments...
  local label=$1 kind=$2 profile=$3 est=$4 pairs=$5
  shift 5
  S_LABEL+=("$label"); S_KIND+=("$kind"); S_PROFILE+=("$profile"); S_EST+=("$est"); S_PAIRS+=("$pairs")
  S_CMD+=("$(printf '%q ' "$@")")
}

wanted() { # label: honours --only and --skip
  local x
  if [ ${#only[@]} -gt 0 ]; then
    for x in "${only[@]}"; do [ "$x" = "$1" ] && return 0; done
    return 1
  fi
  if [ ${#skip[@]} -gt 0 ]; then
    for x in "${skip[@]}"; do [ "$x" = "$1" ] && return 1; done
  fi
  return 0
}

build_jvm() {
  local line label suites pairs
  for line in $JVM_GROUPS; do
    label=${line%%:*}; suites=${line#*:}
    pairs=$(group_pairs "$suites" jvm)
    add_step "$label" jvm full $((pairs * JVM_PAIR_S + GRADLE_STEP_S)) "$pairs" \
      :benchmarks:jvmFullBenchmark "$(include_arg "$suites")"
  done
}

build_jvm_changed() {
  local line label suites pairs
  for line in $JVM_GROUPS; do
    label=${line%%:*}; suites=${line#*:}
    case " $JVM_CHANGED " in *" $label "*) ;; *) continue ;; esac
    pairs=$(group_pairs "$suites" jvm)
    add_step "$label" jvm full $((pairs * JVM_PAIR_S + GRADLE_STEP_S)) "$pairs" \
      :benchmarks:jvmFullBenchmark "$(include_arg "$suites")"
  done
}

build_native() {
  local line label suites pairs
  add_step cold-start cold cold 420 0 :benchmarks:coldStartBenchmark -Pcoldstart.rounds=80
  for line in $NATIVE_GROUPS; do
    label=${line%%:*}; suites=${line#*:}
    pairs=$(group_pairs "$suites" native)
    add_step "$label" native full $((pairs * NATIVE_PAIR_S + GRADLE_STEP_S)) "$pairs" \
      :benchmarks:macosArm64FullBenchmark "$(include_arg "$suites")"
  done
}

build_web() {
  local suite pairs corpora
  for suite in $WEB_SUITES; do
    pairs=$(suite_pairs "$suite" web)
    corpora=$(corpus_values "$suite" 1)
    for platform in js wasm; do
      if [ -n "$corpora" ]; then
        add_step "$platform-$suite" web quick $((pairs * WEB_PAIR_S + GRADLE_STEP_S)) "$pairs" \
          ":benchmarks:${platform/wasm/wasmJs}QuickBenchmark" "$(include_arg "$suite")" "-Pbench.corpus=$corpora"
      else
        add_step "$platform-$suite" web quick $((pairs * WEB_PAIR_S + GRADLE_STEP_S)) "$pairs" \
          ":benchmarks:${platform/wasm/wasmJs}QuickBenchmark" "$(include_arg "$suite")"
      fi
    done
  done
}

# Disputed pairs of an earlier run, three runs each, round by round so that the drift of the machine falls on all of them.
build_remeasure() {
  local n
  for n in 1 2 3; do
    add_step "rr-wasm-tz-$n" web quick $((4 * WEB_PAIR_S + GRADLE_STEP_S)) 4 \
      :benchmarks:wasmJsQuickBenchmark '-Pbench.include=\.tz\.TzBenchmark\.(offsetAt|toInstant)$'
    add_step "rr-wasm-treeReadAll-$n" web quick $((2 * WEB_PAIR_S + GRADLE_STEP_S)) 2 \
      :benchmarks:wasmJsQuickBenchmark '-Pbench.include=\.parse\.ParseBenchmark\.treeReadAll$' \
      -Pbench.corpus=personal,university
    add_step "rr-jvm-byteStable-invite-$n" jvm full $((JVM_PAIR_S + GRADLE_STEP_S)) 1 \
      :benchmarks:jvmFullBenchmark '-Pbench.include=\.write\.WriteBenchmark\.byteStable$' -Pbench.corpus=invite
  done
}

# One quick step per suite and platform on the smallest corpus: checks the regular expressions, the corpus lists and every
# platform toolchain without the cost of the real run.
build_smoke() { # platforms...
  local suite corpus platform task args
  add_step cold-start cold cold 120 0 :benchmarks:coldStartBenchmark -Pcoldstart.rounds=2
  for platform in "$@"; do
    task=":benchmarks:${platform}QuickBenchmark"
    for suite in $WEB_SUITES; do
      corpus=$(smallest_corpus "$suite")
      args=("$task" "$(include_arg "$suite")")
      [ -n "$corpus" ] && args+=("-Pbench.corpus=$corpus")
      add_step "smoke-$platform-$suite" smoke quick $((GRADLE_STEP_S + 60)) 1 "${args[@]}"
    done
  done
}

case "$set_name" in
  jvm)
    if [ "$smoke" -eq 1 ]; then
      add_step jmh-smoke jmh-smoke jmh-smoke 900 0 :benchmarks:jvmFullBenchmark
    else
      build_jvm
    fi
    ;;
  native-web)
    if [ "$smoke" -eq 1 ]; then build_smoke macosArm64 js wasmJs; else build_native; build_web; build_remeasure; build_jvm_changed; fi
    ;;
  web)
    if [ "$smoke" -eq 1 ]; then build_smoke js wasmJs; else build_web; build_remeasure; fi
    ;;
esac

# ---------------------------------------------------------------------------------------------------------------------
# Dry run: the plan and the estimate.

fmt_hm() { printf '%dh%02dm' $(($1 / 3600)) $((($1 % 3600) / 60)); }

print_plan() { # [verbose]
  local i total=0 shown=0
  printf '%-36s %-8s %-6s %6s %9s\n' label kind prof pairs estimate
  for i in "${!S_LABEL[@]}"; do
    wanted "${S_LABEL[$i]}" || continue
    printf '%-36s %-8s %-6s %6s %9s\n' "${S_LABEL[$i]}" "${S_KIND[$i]}" "${S_PROFILE[$i]}" "${S_PAIRS[$i]}" \
      "$(fmt_hm "${S_EST[$i]}")"
    [ "${1:-}" = verbose ] && echo "    ./gradlew ${S_CMD[$i]}--console=plain"
    total=$((total + S_EST[i])); shown=$((shown + 1))
  done
  echo "steps: $shown, estimated time: $(fmt_hm "$total") (night limit $(fmt_hm "$NIGHT_LIMIT_S"))"
  if [ "$total" -gt "$NIGHT_LIMIT_S" ]; then echo "WARNING: the estimate does not fit one night; split the run with --only/--skip"; fi
  if [ "$total" -gt $((NIGHT_LIMIT_S - 1800)) ] && [ "$total" -le "$NIGHT_LIMIT_S" ]; then
    echo "NOTE: less than 30 minutes of margin; a repeated step may run past the night"
  fi
}

if [ "$dry_run" -eq 1 ]; then
  print_plan verbose
  exit 0
fi

# ---------------------------------------------------------------------------------------------------------------------
# The stand.

out="benchmarks/build/campaign/$(date -u +%Y%m%dT%H%M%SZ)$([ "$smoke" -eq 1 ] && echo -smoke || true)"
mkdir -p "$out"
summary="$out/summary.txt"
conditions="$out/conditions.log"
battery_log="$out/battery.log"
: > "$battery_log"

log_cond() { echo "$(date -u +%FT%TZ) $*" | tee -a "$conditions" > /dev/null; }

on_ac() { pmset -g batt 2>/dev/null | head -1 | grep -q "AC Power"; }

# Names of the forbidden programs that run, found by the path of the executable: a process that only mentions Safari in its
# arguments (SafariWidgetExtension) must not count.
forbidden_running() {
  ps -axo comm= 2>/dev/null |
    grep -E '/(Google Chrome|Safari|Telegram|Xcode[^/]*|Android Studio[^/]*|IntelliJ IDEA[^/]*|VLC|Spotify|zoom\.us)\.app/Contents/MacOS/' |
    sed -E 's#.*/([^/]+)\.app/Contents/MacOS/.*#\1#' | sort -u | tr '\n' ' ' || true
}

gradle_daemons() {
  ps -axo args= 2>/dev/null | grep -c 'org\.gradle\.launcher\.daemon\.bootstrap\.GradleDaemon' || true
}

# Power monitor: a line for every tick that sees the battery and for every gap that says the Mac slept.
monitor_pid=""
start_monitor() {
  (
    last=$(date +%s)
    while true; do
      sleep 10
      now=$(date +%s)
      if ! on_ac; then echo "$(date -u +%FT%TZ) battery" >> "$battery_log"; fi
      if [ $((now - last)) -gt 40 ]; then echo "$(date -u +%FT%TZ) gap $((now - last)) s (sleep?)" >> "$battery_log"; fi
      last=$now
    done
  ) &
  monitor_pid=$!
}
cleanup() { [ -n "$monitor_pid" ] && kill "$monitor_pid" 2>/dev/null || true; }
trap cleanup EXIT

wait_for_stand() { # label
  local waited=0 reason
  while true; do
    reason=""
    on_ac || reason="battery"
    local apps; apps=$(forbidden_running)
    [ -n "$apps" ] && reason="$reason running: $apps"
    local daemons; daemons=$(gradle_daemons)
    [ "$daemons" -gt 1 ] && reason="$reason gradle daemons: $daemons"
    if [ -z "$reason" ]; then log_cond "$1 OK"; return 0; fi
    log_cond "$1 WAIT ${reason# }"
    echo "$(date -u +%T) $1: waiting for the stand (${reason# })"
    if [ "$waited" -ge "$WAIT_LIMIT_S" ]; then
      echo "REFUSED $1: the stand was not ready for $((WAIT_LIMIT_S / 3600)) hours" | tee -a "$summary"
      return 1
    fi
    sleep 60
    waited=$((waited + 60))
  done
}

# ---------------------------------------------------------------------------------------------------------------------
# JMH smoke on every JVM pair: the command line of the Gradle task, with the profile of the smoke.

jmh_smoke() { # directory
  local dir=$1 log="$1/gradle-command.log" regex line prefix jvmargs tokens=() all
  all=$(echo "$JVM_GROUPS" | sed -n 's/^[^:]*://p' | tr '\n' '|' | sed 's/|$//')
  regex="io\\.github\\.calendrum\\.benchmarks\\.($all)\\."
  # A filter that matches nothing makes the task print its command line and stop at once.
  ./gradlew :benchmarks:jvmFullBenchmark '-Pbench.include=^$' --info --console=plain > "$log" 2>&1 || true
  line=$(grep -m1 'Command: .* org\.openjdk\.jmh\.Main ' "$log" || true)
  if [ -z "$line" ]; then echo "no JMH command line in $log" >&2; return 1; fi
  line=${line#*Command: }
  prefix=${line%% org.openjdk.jmh.Main *}
  jvmargs=${line##* -jvmArgs }
  read -r -a tokens <<< "$prefix org.openjdk.jmh.Main"
  TZ=UTC "${tokens[@]}" "$regex" -wi 0 -i 1 -r 100ms -f 1 -prof gc -foe true \
    -rf json -rff "$dir/jvm-smoke.json" -jvmArgs "$jvmargs" > "$dir/jmh.log" 2>&1
}

# ---------------------------------------------------------------------------------------------------------------------
# Running a step.

copy_reports() { # label kind profile marker
  local dest="$out/$1" run
  mkdir -p "$dest"
  case "$3" in
    cold) run=$(find "$REPORTS/cold-start" -mindepth 1 -maxdepth 1 -type d -newer "$4" 2>/dev/null | sort | tail -1) ;;
    full|quick) run=$(find "$REPORTS/$3" -mindepth 1 -maxdepth 1 -type d -newer "$4" 2>/dev/null | sort | tail -1) ;;
    *) run="" ;;
  esac
  [ -n "$run" ] && cp -R "$run" "$dest/report"
  if [ "$3" = full ] || [ "$3" = quick ]; then
    [ "$REPORTS/summary.md" -nt "$4" ] && cp "$REPORTS/summary.md" "$dest/summary.md" || true
  fi
  return 0
}

run_step() { # index
  local i=$1 label="${S_LABEL[$1]}" attempt rc spoiled before after start end marker log
  mkdir -p "$out/$label"
  for attempt in 1 2; do
    if [ "$smoke" -eq 0 ]; then wait_for_stand "$label" || exit 3; fi
    log_cond "$label before: $(pmset -g batt 2>/dev/null | tr '\n' ' ')"
    marker="$out/.marker-$label"
    touch "$marker"
    before=$(wc -c < "$battery_log" | tr -d ' ')
    start=$(date -u +%FT%TZ)
    log="$out/$label/gradle$([ "$attempt" -gt 1 ] && echo "-$attempt" || true).log"
    echo "$start $label: attempt $attempt"
    rc=0
    if [ "${S_KIND[$i]}" = jmh-smoke ]; then
      jmh_smoke "$out/$label" || rc=$?
    else
      eval "./gradlew ${S_CMD[$i]} --console=plain" > "$log" 2>&1 || rc=$?
    fi
    end=$(date -u +%FT%TZ)
    after=$(wc -c < "$battery_log" | tr -d ' ')
    spoiled=no
    [ "$smoke" -eq 0 ] && [ "$after" -gt "$before" ] && spoiled=yes
    log_cond "$label after: $(pmset -g batt 2>/dev/null | tr '\n' ' ')"
    copy_reports "$label" "${S_KIND[$i]}" "${S_PROFILE[$i]}" "$marker"
    rm -f "$marker"
    printf '%s\t%s\t%s\trc=%s\tspoiled=%s\tattempt=%s\n' "$label" "$start" "$end" "$rc" "$spoiled" "$attempt" >> "$summary"
    if [ "$spoiled" = no ]; then return 0; fi
    echo "$label: spoiled (battery or sleep during the step), repeating"
  done
}

# ---------------------------------------------------------------------------------------------------------------------

{
  echo "campaign: $set_name$([ "$smoke" -eq 1 ] && echo ' (smoke)' || true), started $(date -u +%FT%TZ)"
  echo "host: $(sw_vers -productName) $(sw_vers -productVersion) $(uname -m); $(java -version 2>&1 | head -1)"
  echo "label	start	end	rc	spoiled	attempt"
} | tee "$summary"
print_plan > "$out/plan.txt"

if [ "$smoke" -eq 0 ]; then
  # A clean slate: at most one daemon is allowed later, and an idle one of an earlier session would block the night.
  ./gradlew --stop --console=plain > /dev/null 2>&1 || true
fi
start_monitor

ran=0
for i in "${!S_LABEL[@]}"; do
  wanted "${S_LABEL[$i]}" || continue
  run_step "$i"
  ran=$((ran + 1))
done

# The last attempt of every step decides.
bad=$(awk -F'\t' '/rc=/ { last[$1] = $0 } END { for (k in last) if (last[k] ~ /rc=[1-9]|spoiled=yes/) n++; print n + 0 }' "$summary")
echo "campaign done $(date -u +%FT%TZ): $ran steps, failed or spoiled steps: $bad" | tee -a "$summary"
[ "$bad" -eq 0 ] || exit 1
