#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF'
Usage: third_party/update.sh [--tzdb VERSION|latest|pinned] [--cldr TAG|latest|pinned]

Replaces the pinned IANA tzdb files and CLDR windowsZones.xml and rewrites third_party/README.md with the versions,
sources and SHA-256 checksums. With no options, only README.md is regenerated from the files in place.

  --tzdb VERSION   a tzdb release such as 2026e, "latest" for the newest one, "pinned" to keep the current files
  --cldr TAG       a CLDR release tag such as release-48, "latest" for the newest final release, "pinned" to keep it

Prints "tzdb OLD -> NEW" and "cldr OLD -> NEW" lines; the exit status is 0 whether or not anything changed.
EOF
}

script_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
tzdb_dir="$script_dir/tzdb"
cldr_dir="$script_dir/cldr"
tzdb_files="africa antarctica asia australasia europe northamerica southamerica etcetera backward zone1970.tab zone.tab iso3166.tab version NEWS LICENSE"
tzdb_request=pinned
cldr_request=pinned

while (($#)); do
  case "$1" in
    --tzdb) tzdb_request=$2; shift 2 ;;
    --cldr) cldr_request=$2; shift 2 ;;
    -h|--help) usage; exit 0 ;;
    *) usage >&2; exit 2 ;;
  esac
done

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

old_tzdb=$(cat "$tzdb_dir/version")
old_cldr=$(cat "$cldr_dir/RELEASE")

if [[ "$tzdb_request" != pinned ]]; then
  if [[ "$tzdb_request" == latest ]]; then
    url="https://data.iana.org/time-zones/tzdata-latest.tar.gz"
  else
    url="https://data.iana.org/time-zones/releases/tzdata${tzdb_request}.tar.gz"
  fi
  curl -fsSL -o "$work/tzdata.tar.gz" "$url"
  mkdir "$work/tzdata"
  tar -xzf "$work/tzdata.tar.gz" -C "$work/tzdata"
  new_tzdb=$(cat "$work/tzdata/version")
  if [[ "$new_tzdb" != "$old_tzdb" ]]; then
    curl -fsSL -o "$work/release.tar.gz" "https://data.iana.org/time-zones/releases/tzdata${new_tzdb}.tar.gz"
    for file in $tzdb_files; do cp "$work/tzdata/$file" "$tzdb_dir/$file"; done
    sha256sum "$work/release.tar.gz" | cut -d' ' -f1 > "$tzdb_dir/ARCHIVE_SHA256"
  fi
  echo "tzdb $old_tzdb -> $new_tzdb"
fi

if [[ "$cldr_request" != pinned ]]; then
  tag=$cldr_request
  if [[ "$tag" == latest ]]; then
    tag=$(curl -fsSL "https://api.github.com/repos/unicode-org/cldr/releases?per_page=50" \
      | grep -o '"tag_name": "release-[0-9]*"' | head -1 | cut -d'"' -f4)
  fi
  if [[ "$tag" != "$old_cldr" ]]; then
    curl -fsSL -o "$cldr_dir/windowsZones.xml" \
      "https://raw.githubusercontent.com/unicode-org/cldr/$tag/common/supplemental/windowsZones.xml"
    curl -fsSL -o "$cldr_dir/LICENSE" "https://raw.githubusercontent.com/unicode-org/cldr/$tag/LICENSE"
    echo "$tag" > "$cldr_dir/RELEASE"
  fi
  echo "cldr $old_cldr -> $tag"
fi

tzdb_version=$(cat "$tzdb_dir/version")
cldr_release=$(cat "$cldr_dir/RELEASE")
archive_sha=$(cat "$tzdb_dir/ARCHIVE_SHA256")
checksums=$(cd "$script_dir" && sha256sum tzdb/LICENSE tzdb/NEWS tzdb/africa tzdb/antarctica tzdb/asia tzdb/australasia \
  tzdb/backward tzdb/etcetera tzdb/europe tzdb/iso3166.tab tzdb/northamerica tzdb/southamerica tzdb/version tzdb/zone.tab \
  tzdb/zone1970.tab cldr/LICENSE cldr/windowsZones.xml)

cat > "$script_dir/README.md" <<EOF
# Pinned third-party data

Time zone data is pinned in the repository so that the build never depends on the network and every build of a version
produces the same tables. The tables in \`calendrum-core\` (aliases) and \`calendrum-tzdata\`
(all zones) are generated from these files by \`build-logic\`. To update, run the \`tzdata-update\` workflow or
\`third_party/update.sh --tzdb latest --cldr latest\`; the script also rewrites this file.

| Directory | Data | Version | Source | License |
|---|---|---|---|---|
| \`tzdb/\` | IANA time zone database: zone and rule files, links, \`zone1970.tab\`, \`zone.tab\`, \`iso3166.tab\`, \`NEWS\` | ${tzdb_version} | \`https://data.iana.org/time-zones/releases/tzdata${tzdb_version}.tar.gz\`, SHA-256 \`${archive_sha}\` | public domain (\`tzdb/LICENSE\`) |
| \`cldr/\` | Unicode CLDR \`windowsZones.xml\`: Windows time zone ids and display names to IANA | ${cldr_release} | \`https://raw.githubusercontent.com/unicode-org/cldr/${cldr_release}/common/supplemental/windowsZones.xml\` | Unicode License v3 (\`cldr/LICENSE\`) |

Only these files of the tzdb archive are kept: \`africa\`, \`antarctica\`, \`asia\`, \`australasia\`, \`europe\`, \`northamerica\`,
\`southamerica\`, \`etcetera\`, \`backward\`, \`zone1970.tab\`, \`zone.tab\`, \`iso3166.tab\`, \`version\`, \`NEWS\`, \`LICENSE\`. They are
byte-for-byte copies from the archive. \`tzdb/ARCHIVE_SHA256\` and \`cldr/RELEASE\` record the archive checksum and the CLDR tag.

SHA-256 of the pinned files:

\`\`\`
${checksums}
\`\`\`
EOF
