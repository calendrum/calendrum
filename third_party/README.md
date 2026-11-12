# Pinned third-party data

Time zone data is pinned in the repository so that the build never depends on the network and every build of a version
produces the same tables. The tables in `calendrum-core` (aliases) and `calendrum-tzdata`
(all zones) are generated from these files by `build-logic`. To update, run the `tzdata-update` workflow or
`third_party/update.sh --tzdb latest --cldr latest`; the script also rewrites this file.

| Directory | Data | Version | Source | License |
|---|---|---|---|---|
| `tzdb/` | IANA time zone database: zone and rule files, links, `zone1970.tab`, `zone.tab`, `iso3166.tab`, `NEWS` | 2026e | `https://data.iana.org/time-zones/releases/tzdata2026e.tar.gz`, SHA-256 `b26882805f26aac59d5b222978e6580484b834ccdc98be89df2f05a6dc53a652` | public domain (`tzdb/LICENSE`) |
| `cldr/` | Unicode CLDR `windowsZones.xml`: Windows time zone ids and display names to IANA | release-48 | `https://raw.githubusercontent.com/unicode-org/cldr/release-48/common/supplemental/windowsZones.xml` | Unicode License v3 (`cldr/LICENSE`) |

Only these files of the tzdb archive are kept: `africa`, `antarctica`, `asia`, `australasia`, `europe`, `northamerica`,
`southamerica`, `etcetera`, `backward`, `zone1970.tab`, `zone.tab`, `iso3166.tab`, `version`, `NEWS`, `LICENSE`. They are
byte-for-byte copies from the archive. `tzdb/ARCHIVE_SHA256` and `cldr/RELEASE` record the archive checksum and the CLDR tag.

SHA-256 of the pinned files:

```
0613408568889f5739e5ae252b722a2659c02002839ad970a63dc5e9174b27cf  tzdb/LICENSE
b1aecd6fe12b04ca145395ff52b9550e5315a1126da49b35f3637a2fe915501c  tzdb/NEWS
f2851d4be4a4925cbdc9d56e10d780bccadb89d6ffb9aed78c3e35f97c200aed  tzdb/africa
e410ad71c9450828c592d21419301d41ac79ce50159fd0ac2d6c5031cb6bdfe6  tzdb/antarctica
02b69728085f6b0c999da70de2f1f9a2d8952b533a450d53bb41e03eb7d0e92e  tzdb/asia
846ba455578e3e0f9eb850f05be3cc06d02ff51bbe4b726f755be9dc765f16c9  tzdb/australasia
b8296b9ef1ece72d813878132c0f17e2c52b26af6004eeb9c37d049b64cd9401  tzdb/backward
7281f095b42c13c4ae36b8bcba884e81dbb38127221fc1d9805c4dbf852487db  tzdb/etcetera
fec11bf6c6bd0619a8777bbdaa2ecd184fde291cb4b97efa87d334c7145ba3c2  tzdb/europe
837c80785080c8433fd9d4ea87e78f161ac7a40389301c5153d4f90198baeb2a  tzdb/iso3166.tab
c0c5da0dc337444d80709cf9f8d8011719b789df160ce107a5c38c90e73dc54c  tzdb/northamerica
13be56a242da3c1a4a6d12050e5fd48b1753c8c1dc237cbc2edb40eac636be7f  tzdb/southamerica
af35b79aa1b21f258217dc39db156c783db83fd78afd095c5426061993877063  tzdb/version
e2e8e31c4e10f44576c79345e76060591dbb3eef6ce14e006afc8a373caa9593  tzdb/zone.tab
5f23781702bf51408ff1581daa109ab3b9257fb433b1554e579d92172892c0cf  tzdb/zone1970.tab
b4c0ae8ef04f7059f96ce5bbe0467f9fe6f6d81bbe13517701dfeb961fb4d0b6  cldr/LICENSE
9cf3db6a31fb382fee21b70be6feba1e82766b0fcd06e6261fb7936a73e537ff  cldr/windowsZones.xml
```
