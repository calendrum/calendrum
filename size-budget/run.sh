#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF_USAGE'
Usage: size-budget/run.sh [--skip-publish] [--update-baseline] [--platforms LIST]

Measures what Calendrum adds to a consumer app on JVM (+R8), JS, wasmJs, Android and iOS
and checks the contribution against size-budget/baseline.json.

  --skip-publish      do not run publishToMavenLocal in the root build first
  --update-baseline   accept the measured contributions: rewrite them in size-budget/baseline.json,
                      keeping the values of platforms that were not measured
  --platforms LIST    comma-separated platforms to measure: jvm,js,wasmJs,android,ios.
                      Default: every platform available on this host (Android needs the Android SDK,
                      iOS needs macOS). A selected platform that is not available is an error.

The Android SDK is taken from ANDROID_HOME, then ANDROID_SDK_ROOT, then ~/Library/Android/sdk.

The report is size-budget/build/size-report.md (and .json). With GITHUB_STEP_SUMMARY set,
the report is appended to the job summary.
EOF_USAGE
}

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
root_dir=$(dirname "$script_dir")
report="$script_dir/build/size-report.md"

skip_publish=false
update_baseline=false
platforms=""

while (($#)); do
  case "$1" in
    --skip-publish) skip_publish=true ;;
    --update-baseline) update_baseline=true ;;
    --platforms)
      if (($# < 2)); then
        echo "--platforms needs a value" >&2
        exit 2
      fi
      platforms=$2
      shift
      ;;
    --platforms=*) platforms=${1#--platforms=} ;;
    -h | --help)
      usage
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      usage >&2
      exit 2
      ;;
  esac
  shift
done

if [ -z "${ANDROID_HOME:-}" ] && [ -z "${ANDROID_SDK_ROOT:-}" ] && [ -d "$HOME/Library/Android/sdk" ]; then
  export ANDROID_HOME="$HOME/Library/Android/sdk"
fi
echo "==> Android SDK: ${ANDROID_HOME:-${ANDROID_SDK_ROOT:-not found}}"

if ! $skip_publish; then
  echo "==> publishToMavenLocal"
  (cd "$root_dir" && ./gradlew --console=plain publishToMavenLocal)
fi

rm -f "$report" "$script_dir/build/size-report.json"

args=(--console=plain -p size-budget sizeReport)
if $update_baseline; then
  args+=(-PupdateBaseline=true)
fi
if [ -n "$platforms" ]; then
  args+=("-PsizePlatforms=$platforms")
fi

echo "==> sizeReport"
status=0
(cd "$root_dir" && ./gradlew "${args[@]}") || status=$?

if [ -f "$report" ]; then
  echo
  cat "$report"
  if [ -n "${GITHUB_STEP_SUMMARY:-}" ]; then
    cat "$report" >>"$GITHUB_STEP_SUMMARY"
  fi
fi

exit "$status"
