#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF'
Usage: sdk/c/run.sh [--skip-publish] [--bench]

Builds the C SDK spike (a Kotlin/Native shared library with a C facade) against Calendrum in the local Maven repository,
runs the Python (ctypes) tests and, with --bench, the C, Python and Kotlin benchmarks.

  --skip-publish   do not run publishToMavenLocal in the root build first
  --bench          also run the benchmarks (macOS arm64 only)
EOF
}

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
root_dir=$(cd "$script_dir/../.." && pwd)
build_dir="$script_dir/build"
sample="$root_dir/examples/shared/sample.ics"

skip_publish=false
bench=false
while (($#)); do
  case "$1" in
    --skip-publish) skip_publish=true ;;
    --bench) bench=true ;;
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

case "$(uname -s)-$(uname -m)" in
  Darwin-arm64) target=MacosArm64 dir=macosArm64 library=libcalendrum.dylib ;;
  Linux-x86_64) target=LinuxX64 dir=linuxX64 library=libcalendrum.so ;;
  *)
    echo "The C SDK spike builds on macOS arm64 and Linux x64 only" >&2
    exit 1
    ;;
esac

if ! $skip_publish; then
  echo "==> publishToMavenLocal"
  (cd "$root_dir" && ./gradlew --console=plain publishToMavenLocal)
fi

tasks=(linkReleaseSharedMacosArm64 linkReleaseSharedLinuxX64)
if $bench; then
  tasks+=(linkBenchReleaseExecutableMacosArm64)
fi
echo "==> ${tasks[*]}"
"$root_dir/gradlew" --console=plain -p "$script_dir" "${tasks[@]}"

echo "==> Python tests"
(cd "$script_dir/python" && CALENDRUM_LIBRARY="$build_dir/bin/$dir/releaseShared/$library" python3 -m unittest -v test_calendrum)

echo
echo "Sizes, bytes"
for file in "$build_dir/bin/macosArm64/releaseShared/libcalendrum.dylib" "$build_dir/bin/linuxX64/releaseShared/libcalendrum.so"; do
  [ -f "$file" ] || continue
  size=$(wc -c <"$file" | tr -d ' ')
  gzipped=$(gzip -9 -c "$file" | wc -c | tr -d ' ')
  stripped=""
  if [[ $file == *.dylib ]] && command -v strip >/dev/null 2>&1; then
    strip -x -o "$build_dir/stripped.dylib" "$file"
    stripped=", strip -x $(wc -c <"$build_dir/stripped.dylib" | tr -d ' ')"
  fi
  echo "$(basename "$file") $size, gzip -9 $gzipped$stripped"
done

if $bench; then
  if [ "$target" != MacosArm64 ]; then
    echo "Benchmarks run on macOS arm64 only" >&2
    exit 1
  fi
  mkdir -p "$build_dir/bench"
  python3 "$script_dir/bench/make_feed.py" "$build_dir/bench/feed-200.ics" 200
  shared="$build_dir/bin/macosArm64/releaseShared"
  clang -O2 -I"$script_dir/include" "$script_dir/bench/bench.c" -L"$shared" -lcalendrum -Wl,-rpath,"$shared" \
    -o "$build_dir/bench/bench"
  kexe="$build_dir/bin/macosArm64/benchReleaseExecutable/bench.kexe"
  for input in "$sample:20000" "$build_dir/bench/feed-200.ics:300"; do
    file=${input%:*}
    rounds=${input##*:}
    echo
    echo "==> $(basename "$file"), $rounds rounds"
    "$kexe" "$file" "$rounds"
    "$build_dir/bench/bench" "$file" "$rounds"
    (cd "$script_dir/python" && python3 bench.py "$file" "$rounds")
  done
fi
