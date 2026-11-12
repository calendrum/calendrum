#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF'
Usage: examples/run.sh [--skip-publish] [EXAMPLE...]

Builds and tests the example projects in examples/ against Calendrum in the local Maven repository.

  --skip-publish   do not run publishToMavenLocal in the root build first
  EXAMPLE...       cli, ktor-feed-server, android-compose, ios-swiftui; all if none are given

android-compose needs an Android SDK (ANDROID_HOME, ANDROID_SDK_ROOT or ~/Library/Android/sdk).
ios-swiftui needs macOS with Xcode and an iPhone simulator. Examples that the host cannot build are skipped.
EOF
}

all_examples=(cli ktor-feed-server android-compose ios-swiftui)

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
root_dir=$(dirname "$script_dir")
build_dir="$script_dir/build"

skip_publish=false
selected=()
while (($#)); do
  case "$1" in
    --skip-publish) skip_publish=true ;;
    -h | --help)
      usage
      exit 0
      ;;
    -*)
      echo "Unknown option: $1" >&2
      usage >&2
      exit 2
      ;;
    *)
      if [[ " ${all_examples[*]} " != *" $1 "* ]]; then
        echo "Unknown example '$1', expected one of: ${all_examples[*]}" >&2
        exit 2
      fi
      selected+=("$1")
      ;;
  esac
  shift
done
if ((${#selected[@]} == 0)); then
  selected=("${all_examples[@]}")
fi

if [ -z "${ANDROID_HOME:-}" ] && [ -z "${ANDROID_SDK_ROOT:-}" ] && [ -d "$HOME/Library/Android/sdk" ]; then
  export ANDROID_HOME="$HOME/Library/Android/sdk"
fi
android_sdk=${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}

gradle() {
  "$root_dir/gradlew" --console=plain -p "$@"
}

simulator() {
  xcrun simctl list devices available | sed -n 's/^ *iPhone[^(]*(\([0-9A-F-]\{36\}\)).*/\1/p' | head -n 1
}

# An example the host cannot build (no Android SDK, no Xcode or iPhone simulator) is reported as skipped instead of
# failing.
skip_reason() {
  case "$1" in
    android-compose)
      if [ -z "$android_sdk" ] || [ ! -d "$android_sdk" ]; then
        echo "no Android SDK"
      fi
      ;;
    ios-swiftui)
      if [ "$(uname -s)" != "Darwin" ] || ! command -v xcodebuild >/dev/null 2>&1; then
        echo "needs macOS with Xcode"
      elif [ -z "$(simulator)" ]; then
        echo "no iPhone simulator"
      fi
      ;;
  esac
}

run_example() {
  case "$1" in
    cli)
      gradle "$script_dir/cli" build &&
        gradle "$script_dir/cli" run --args="occurrences $script_dir/shared/sample.ics"
      ;;
    ktor-feed-server)
      gradle "$script_dir/ktor-feed-server" build
      ;;
    android-compose)
      gradle "$script_dir/android-compose" :app:testDebugUnitTest :app:assembleDebug
      ;;
    ios-swiftui)
      gradle "$script_dir/ios-swiftui/shared" assembleCalendrumScheduleReleaseXCFramework &&
        (
          cd "$script_dir/ios-swiftui" && xcodebuild test \
            -scheme ScheduleUI \
            -destination "platform=iOS Simulator,id=$(simulator)" \
            -derivedDataPath "$build_dir/ios-swiftui"
        )
      ;;
  esac
}

format_time() {
  printf '%dm%02ds' $(($1 / 60)) $(($1 % 60))
}

if ! $skip_publish; then
  echo "==> publishToMavenLocal"
  (cd "$root_dir" && ./gradlew --console=plain publishToMavenLocal)
fi

mkdir -p "$build_dir/logs"
rm -f "$build_dir"/logs/*.log

results=()
failed=0
for name in "${selected[@]}"; do
  reason=$(skip_reason "$name")
  if [ -n "$reason" ]; then
    results+=("$(printf '%-18s %-8s %8s  %s' "$name" "skipped" "-" "$reason")")
    continue
  fi
  log="$build_dir/logs/$name.log"
  echo "==> $name"
  started=$SECONDS
  if (run_example "$name") 2>&1 | tee "$log"; then
    result=passed
  else
    result=FAILED
    failed=1
  fi
  results+=("$(printf '%-18s %-8s %8s' "$name" "$result" "$(format_time $((SECONDS - started)))")")
done

echo
echo "Examples"
printf '%-18s %-8s %8s\n' "Example" "Result" "Time"
for row in "${results[@]}"; do
  echo "$row"
done
echo
echo "Logs: $build_dir/logs"
exit "$failed"
