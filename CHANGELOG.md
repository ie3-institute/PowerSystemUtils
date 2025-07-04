# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased/Snapshot]

### Changed
- Reincluded SonarQube step for Dependabot PRs [#575](https://github.com/ie3-institute/PowerSystemUtils/issues/575)

## [3.1.0]

### Added
- Added auto-approval to dependabot workflow and restrictions to updates [#551](https://github.com/ie3-institute/PowerSystemUtils/issues/551)

### Changed
- Bumped scala to 3.7.0 [#561](https://github.com/ie3-institute/PowerSystemUtils/pull/561)

### Fixed
- Fixed missing scala sources in releases [#549](https://github.com/ie3-institute/PowerSystemUtils/issues/549)
- Fixed missing if-block in Version-Check [#557](https://github.com/ie3-institute/PowerSystemUtils/issues/557)
- Improved the fix of Version Check [#559](https://github.com/ie3-institute/PowerSystemUtils/issues/559)

## [3.0.0]

### Added
- Added semVer Gradle plugin to enable semantic versioning [#544](https://github.com/ie3-institute/PowerSystemUtils/issues/544)
- Implemented GitHub Actions Pipeline [#540](https://github.com/ie3-institute/PowerSystemUtils/issues/540)

### Changed
- Added Bao and Staudt to the list of reviewers [#510](https://github.com/ie3-institute/PowerSystemUtils/issues/510)
- Upgraded to `scala3` [#533](https://github.com/ie3-institute/PowerSystemUtils)

### Fixed
- Fixed deployment in GitHub Actions pipeline and removed Jenkinsfile [#546](https://github.com/ie3-institute/PowerSystemUtils/issues/546)

## [2.2.1]

### Changed
- `GeoUtils.calcOrderedCoordinateDistances()` now returns a manually sorted `List` instead of a `SortedSet` [#449](https://github.com/ie3-institute/PowerSystemUtils/issues/449)

### Fixed
- Bug where `GeoUtils.calcOrderedCoordinateDistances()` didn't return all `CoordinateDistance` [#449](https://github.com/ie3-institute/PowerSystemUtils/issues/449)

## [2.2.0]

### Changed
-  Remove unused fields from TimeUtil [#442](https://github.com/ie3-institute/PowerSystemUtils/issues/442)

## [2.1.0]

### Added
- Added quantity for volumetric flow rate [#363](https://github.com/ie3-institute/PowerSystemUtils/issues/363)

### Fixed
- `TimeUtil` changes time zone when parsing `ZonedDateTime` [#422](https://github.com/ie3-institute/PowerSystemUtils/issues/422)

### Breaking
- Changed default date time formatter to ISO 8601 ISO_OFFSET_DATE_TIME [#415](https://github.com/ie3-institute/PowerSystemUtils/issues/415)

### Changed
- Updated to gradle 8.5 [#425](https://github.com/ie3-institute/PowerSystemUtils/issues/425)
- Updated to gradle 8.6 [#437](https://github.com/ie3-institute/PowerSystemUtils/issues/437)

## [2.0.0]

### Added
- Added implicit classes for `loactiontec.jts` Geometries that represent geographical geometries with functionality before located in `GeoUtils` [#163](https://github.com/ie3-institute/PowerSystemUtils/issues/163)
- `OsmEntity` and `OsmContainer` to provide a simple, lightweight representation of openstreetmap data
- QuantityUtils previously implemented in SIMONA [#288](https://github.com/ie3-institute/PowerSystemUtils/issues/288)
- Enhanced `RichQuantityDouble` with new units and generic method [#312](https://github.com/ie3-institute/PowerSystemUtils/issues/312), [#322](https://github.com/ie3-institute/PowerSystemUtils/issues/322)
- Calculation of bounding box to `GeoUtils` [#320](https://github.com/ie3-institute/PowerSystemUtils/issues/320)

### Changed
- Refactored `GeoUtils`, moved them to the scala package and tailored them toward the `loactiontec.jts` Geometries used in the `OsmContainer` [#163](https://github.com/ie3-institute/PowerSystemUtils/issues/163)
- Changed unit symbols according to DIN 1301-1 for apparent and reactive power [#278](https://github.com/ie3-institute/PowerSystemUtils/issues/278)
- Rounding for quantities is now part of the `RichQuantity` [#314](https://github.com/ie3-institute/PowerSystemUtils/issues/314)
- Power system units rely on fast double unit conversions now [#328](https://github.com/ie3-institute/PowerSystemUtils/issues/328)

### Fixed
- Fix tests in CI [#206](https://github.com/ie3-institute/PowerSystemUtils/issues/206)
  - Enable using JUnit platform
  - Fix broken tests
  - Let scalatest and JUnit tests run together
- Improve code quality to meet minimum standards [#203](https://github.com/ie3-institute/PowerSystemUtils/issues/203)
  - Use `Stream#toList`
  - Enhance deprecation annotations
- Fix formatting for MarkDown files
- Configure gradle jacoco plugin according to newest documentation 
- Fixed badges in README.md [#290](https://github.com/ie3-institute/PowerSystemUtils/issues/290)
- Fix (PowerSystem)Units and converters [#330](https://github.com/ie3-institute/PowerSystemUtils/issues/330)

## [1.6.0]

**Last version with java 8 support!**

### Added
- Added scala support
- Added `RichQuantityDouble` as double type enrichment to enable easy quantity conversions [#133](https://github.com/ie3-institute/PowerSystemUtils/issues/133)

### Fixed
- log4j security update
- dangerous constructor call in `OneToOneMap`

## [1.5.3]
### Fixed
-   Adding dependency constraint to avoid transitive dependencies introducing information vulnerability [CVE-2020-15250 - Temporary folder vulnerability](https://github.com/advisories/GHSA-269g-pwp5-87pp)

## [1.5.2]
### Changed
-   Use Maven Central as repository for dependencies

### Added
-   `Naming` that aids in providing an entity naming respecting different well known naming strategies

## [1.5.1]
### Fixed
-   Malicious implementation of some units + additional tests for transformed units

## [1.5]

### Added
-   Added `buildSafe{Coord,Point,LineString,LineStringBetweenCoords,LineStringBetweenPoints}` and `totalLengthOfLineString` to `GeoUtils`

### Changed
-   Deprecated `PowerDensity` (will be replaced by `Irradiation`) & SI conform usage of `Irradiation` and `EnergyDensity` ([#54](https://github.com/ie3-institute/PowerSystemUtils/issues/54))
-   Minor addition to `TimeUtil`

### Fixed
-   Use constant version numbers for dependencies

## [1.4]

### Added
-   Introduction of EmptyQuantity, a representation for null value Quantities

### Changed
-   Adaptions in QuantityUtil
-   Improved implementation of `StandardUnits.PU`
-   BREAKING: replaced Unit API 1.0 (JSR 363, tec.uom.se) with Unit API 2.0 (JSR 385, tech.units.indriya)

### Removed
-   Deprecated methods in `GeoUtils`
-   Deprecated `TimeTools`

## [1.3.2]

### Added
-   Introduction of new quantity library (+ deprecation of old implementations)

### Changed
-   Adaptions in QuantityUtil
-   Improved version of TimeUtils
-   Improved building of csv strings

### Fixed

## [1.3.1]

### Added
-   added more functionalities to FileIOUtils
-   introduction of QuantityUtil

### Changed
-   renamed FileHelper -> FileIOUtils
-   minor extensions in StringUtils (snake, camel and pascal case)

### Fixed
-   fixes + extensions in StandardUnits

[Unreleased/Snapshot]: https://github.com/ie3-institute/powersystemutils/compare/v3.1.0...HEAD
[3.1.0]: https://github.com/ie3-institute/powersystemutils/compare/v3.0.0...v3.1.0
[3.0.0]: https://github.com/ie3-institute/powersystemutils/compare/v2.2.1...v3.0.0
[2.2.1]: https://github.com/ie3-institute/powersystemutils/compare/v2.2.0...v2.2.1
[2.2.0]: https://github.com/ie3-institute/powersystemutils/compare/v2.1.0...v2.2.0
[2.1.0]: https://github.com/ie3-institute/powersystemutils/compare/v2.0.0...v2.1.0
[2.0.0]: https://github.com/ie3-institute/powersystemutils/compare/v1.6.0...v2.0.0
[1.6.0]: https://github.com/ie3-institute/powersystemutils/compare/v1.5.3...v1.6.0
[1.5.3]: https://github.com/ie3-institute/powersystemutils/compare/v1.5.2...v1.5.3
[1.5.2]: https://github.com/ie3-institute/powersystemutils/compare/v1.4...v1.5.2
[1.5.1]: https://github.com/ie3-institute/powersystemutils/compare/v1.4...v1.5.1
[1.5]: https://github.com/ie3-institute/powersystemutils/compare/v1.4...v1.5
[1.4]: https://github.com/ie3-institute/powersystemutils/compare/v1.3.2...v1.4
[1.3.2]: https://github.com/ie3-institute/powersystemutils/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/ie3-institute/powersystemutils/compare/64283b769d1faeac0a6468b0f225f5e995741cdd...v1.3.1
