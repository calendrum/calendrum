#!/usr/bin/env bash

# Builds the consumer matrix against Calendrum published to the local Maven repository (see --help).
set -euo pipefail

usage() {
  cat <<'EOF'
Usage: compat-tests/run.sh [--skip-publish] [--targets LIST] [CASE...]

Builds consumer projects from compat-tests/cases against Calendrum in the local Maven repository.

  --skip-publish   do not run publishToMavenLocal in the root build first
  --targets LIST   run only these targets of each case, comma-separated (jvm, js, wasmJs, linuxX64);
                   cases without any of them are skipped
  CASE...          case names from compat-tests/cases; all cases if none are given

linuxX64 tests run only on a Linux host; elsewhere linuxX64 is compiled and linked.
EOF
}

known_targets=",jvm,js,wasmJs,linuxX64,"

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
root_dir=$(dirname "$script_dir")
build_dir="$script_dir/build"
cases_file="$script_dir/cases"

skip_publish=false
target_filter=""
selected=()

while (($#)); do
  case "$1" in
    --skip-publish) skip_publish=true ;;
    --targets)
      if (($# < 2)); then
        echo "--targets needs a comma-separated list" >&2
        exit 2
      fi
      target_filter=$2
      shift
      ;;
    --targets=*) target_filter=${1#--targets=} ;;
    -h | --help)
      usage
      exit 0
      ;;
    -*)
      echo "Unknown option: $1" >&2
      usage >&2
      exit 2
      ;;
    *) selected+=("$1") ;;
  esac
  shift
done

if [ -n "$target_filter" ]; then
  IFS=, read -r -a filter_list <<<"$target_filter"
  for target in "${filter_list[@]}"; do
    if [[ $known_targets != *",$target,"* ]]; then
      echo "Unknown target '$target', expected one of: ${known_targets:1:${#known_targets}-2}" >&2
      exit 2
    fi
  done
fi

# calendrumVersion is VERSION_NAME of the root gradle.properties, the version publishToMavenLocal produces.
calendrum_version=$(sed -n 's/^VERSION_NAME=//p' "$root_dir/gradle.properties")
if [ -z "$calendrum_version" ]; then
  echo "No VERSION_NAME in $root_dir/gradle.properties" >&2
  exit 1
fi
jvm_args=$(sed -n 's/^org.gradle.jvmargs=//p' "$script_dir/gradle.properties")

# The cases file has one case per line: name, template (jvm or kmp), Kotlin version, Gradle version, SHA-256 of
# gradle-<version>-bin.zip (services.gradle.org/distributions/gradle-<version>-bin.zip.sha256), comma-separated targets.
# The first line is a header. For 2.2 and 2.3 we take the earliest patch release; Gradle is within the range officially
# supported by the Kotlin Gradle plugin of the case.
case_names=()
case_templates=()
case_kotlin=()
case_gradle=()
case_sha256=()
case_targets=()
while read -r name template kotlin gradle sha256 targets extra; do
  if [ -z "$name" ] || [ "$name" = "case" ]; then
    continue
  fi
  if [ -z "$targets" ] || [ -n "$extra" ]; then
    echo "Malformed line for case '$name' in $cases_file" >&2
    exit 1
  fi
  case_names+=("$name")
  case_templates+=("$template")
  case_kotlin+=("$kotlin")
  case_gradle+=("$gradle")
  case_sha256+=("$sha256")
  case_targets+=("$targets")
done <"$cases_file"

for name in ${selected[@]+"${selected[@]}"}; do
  found=false
  for known in "${case_names[@]}"; do
    if [ "$known" = "$name" ]; then
      found=true
    fi
  done
  if ! $found; then
    echo "Unknown case '$name', expected one of: ${case_names[*]}" >&2
    exit 2
  fi
done

is_selected() {
  if ((${#selected[@]} == 0)); then
    return 0
  fi
  local name
  for name in "${selected[@]}"; do
    if [ "$name" = "$1" ]; then
      return 0
    fi
  done
  return 1
}

# --targets narrows each case to the listed targets; a case left with none is skipped, so `--targets linuxX64` runs only
# the kmp cases.
filter_targets() {
  local result="" target
  local -a list
  IFS=, read -r -a list <<<"$1"
  for target in "${list[@]}"; do
    if [ -z "$target_filter" ] || [[ ",$target_filter," == *",$target,"* ]]; then
      result=${result:+$result,}$target
    fi
  done
  echo "$result"
}

# Tests by task name. Only a Linux host runs linuxX64 tests; elsewhere linuxX64 is compiled and its test binary linked.
gradle_tasks() {
  local template=$1 targets=$2 target
  local -a list
  if [ "$template" = "jvm" ]; then
    echo "build"
    return
  fi
  IFS=, read -r -a list <<<"$targets"
  for target in "${list[@]}"; do
    case "$target" in
      jvm) echo "jvmTest" ;;
      js) echo "jsNodeTest" ;;
      wasmJs) echo "wasmJsNodeTest" ;;
      linuxX64)
        if [ "$(uname -s)" = "Linux" ]; then
          echo "linuxX64Test"
        else
          echo "compileKotlinLinuxX64"
          echo "linkDebugTestLinuxX64"
        fi
        ;;
    esac
  done
}

# A fresh copy per run: the template, the root wrapper script and JAR, and gradle-wrapper.properties for the Gradle of
# the case with its SHA-256, so the wrapper verifies the distribution.
prepare_case() {
  local case_dir=$1 template=$2 gradle=$3 sha256=$4
  rm -rf "$case_dir"
  mkdir -p "$case_dir/gradle/wrapper"
  cp -R "$script_dir/$template/." "$case_dir/"
  cp "$script_dir/gradle.properties" "$case_dir/gradle.properties"
  cp "$root_dir/gradlew" "$case_dir/gradlew"
  cp "$root_dir/gradle/wrapper/gradle-wrapper.jar" "$case_dir/gradle/wrapper/gradle-wrapper.jar"
  cat >"$case_dir/gradle/wrapper/gradle-wrapper.properties" <<EOF
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionSha256Sum=$sha256
distributionUrl=https\://services.gradle.org/distributions/gradle-$gradle-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF
}

format_time() {
  printf '%dm%02ds' $(($1 / 60)) $(($1 % 60))
}

if ! $skip_publish; then
  echo "==> publishToMavenLocal (Calendrum $calendrum_version)"
  (cd "$root_dir" && ./gradlew --console=plain publishToMavenLocal)
fi

mkdir -p "$build_dir/logs"
rm -f "$build_dir"/logs/*.log

results=()
failed=0
for i in "${!case_names[@]}"; do
  name=${case_names[$i]}
  if ! is_selected "$name"; then
    continue
  fi
  targets=$(filter_targets "${case_targets[$i]}")
  row_prefix=$(printf '%-14s %-12s %-7s' "$name" "${case_kotlin[$i]}" "${case_gradle[$i]}")
  if [ -z "$targets" ]; then
    results+=("$(printf '%s %-24s %-8s %8s %8s' "$row_prefix" "-" "skipped" "-" "-")")
    continue
  fi

  case_dir="$build_dir/cases/$name"
  log="$build_dir/logs/$name.log"
  prepare_case "$case_dir" "${case_templates[$i]}" "${case_gradle[$i]}" "${case_sha256[$i]}"
  tasks=()
  while read -r task; do
    tasks+=("$task")
  done < <(gradle_tasks "${case_templates[$i]}" "$targets")

  echo "==> $name: Kotlin ${case_kotlin[$i]}, Gradle ${case_gradle[$i]}, targets $targets: ${tasks[*]}"
  started=$SECONDS

  # A failed case does not stop the matrix; the exit code is non-zero if any case failed.
  if (
    # No daemon: the build JVM exits after the case, which keeps memory low on CI runners. --continue reports all
    # failing targets of a case.
    cd "$case_dir" && ./gradlew --no-daemon --console=plain --continue \
      "-Dorg.gradle.jvmargs=$jvm_args" \
      "-PkotlinVersion=${case_kotlin[$i]}" \
      "-PcalendrumVersion=$calendrum_version" \
      "-PcompatTargets=$targets" \
      "${tasks[@]}"
  ) 2>&1 | tee "$log"; then
    result=passed
  else
    result=FAILED
    failed=1
  fi
  elapsed=$(format_time $((SECONDS - started)))
  warnings=$(grep -cE '^(w: |warning: )' "$log" || true)
  results+=("$(printf '%s %-24s %-8s %8s %8s' "$row_prefix" "$targets" "$result" "$elapsed" "$warnings")")
done

echo
echo "Compatibility of Calendrum $calendrum_version consumers"
printf '%-14s %-12s %-7s %-24s %-8s %8s %8s\n' "Case" "Kotlin" "Gradle" "Targets" "Result" "Time" "Warnings"
for row in ${results[@]+"${results[@]}"}; do
  echo "$row"
done

# Compiler warnings (`w: `) and errors (`e: `) of every case, deduplicated, for the report.
for log in "$build_dir"/logs/*.log; do
  [ -e "$log" ] || continue
  if grep -qE '^(w: |warning: )' "$log"; then
    echo
    echo "Warnings in $(basename "$log" .log):"
    grep -E '^(w: |warning: )' "$log" | awk '!seen[$0]++'
  fi
  if grep -qE '^e: ' "$log"; then
    echo
    echo "Errors in $(basename "$log" .log):"
    grep -E '^e: ' "$log" | awk '!seen[$0]++'
  fi
done

echo
echo "Logs: $build_dir/logs"
exit "$failed"
