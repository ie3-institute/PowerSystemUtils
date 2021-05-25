# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased/Snapshot]

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

[Unreleased/Snapshot]: https://github.com/ie3-institute/powersystemutils/compare/v1.5.3...HEAD
[1.5.3]: https://github.com/ie3-institute/powersystemutils/compare/v1.5.2...v1.5.3
[1.5.2]: https://github.com/ie3-institute/powersystemutils/compare/v1.4...v1.5.2
[1.5.1]: https://github.com/ie3-institute/powersystemutils/compare/v1.4...v1.5.1
[1.5]: https://github.com/ie3-institute/powersystemutils/compare/v1.4...v1.5
[1.4]: https://github.com/ie3-institute/powersystemutils/compare/v1.3.2...v1.4
[1.3.2]: https://github.com/ie3-institute/powersystemutils/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/ie3-institute/powersystemutils/compare/64283b769d1faeac0a6468b0f225f5e995741cdd...v1.3.1
