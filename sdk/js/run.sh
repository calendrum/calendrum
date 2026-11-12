#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF2'
Usage: sdk/js/run.sh [--skip-publish] [ROUNDS]

Builds the JS SDK spike (an @JsExport facade) against Calendrum in the local Maven repository, runs its Kotlin tests on
Node and the TypeScript example (Node 22.18 or newer runs .ts files directly), which also times parse and occurrences.

  --skip-publish   do not run publishToMavenLocal in the root build first
  ROUNDS           rounds for the timings of the example, 2000 by default
EOF2
}

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
root_dir=$(cd "$script_dir/../.." && pwd)

skip_publish=false
rounds=2000
while (($#)); do
  case "$1" in
    --skip-publish) skip_publish=true ;;
    -h | --help)
      usage
      exit 0
      ;;
    *) rounds=$1 ;;
  esac
  shift
done

if ! $skip_publish; then
  echo "==> publishToMavenLocal"
  (cd "$root_dir" && ./gradlew --console=plain publishToMavenLocal)
fi

"$root_dir/gradlew" --console=plain -p "$script_dir" jsNodeTest jsNodeProductionLibraryDistribution

dist="$script_dir/build/dist/js/productionLibrary"
total=0
gzipped=0
for file in "$dist"/*.mjs; do
  total=$((total + $(wc -c <"$file")))
  gzipped=$((gzipped + $(gzip -9 -c "$file" | wc -c)))
done
echo
echo "productionLibrary .mjs: $total bytes, gzip -9 $gzipped bytes"
echo
node "$script_dir/example/main.ts" "$root_dir/examples/shared/sample.ics" "$rounds"
