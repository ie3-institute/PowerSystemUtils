/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.geo

import edu.ie3.util.quantities.QuantityUtil
import org.locationtech.jts.geom.*
import org.locationtech.jts.io.geojson.GeoJsonReader
import spock.lang.Shared
import spock.lang.Specification
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities

import javax.measure.quantity.Length

import static edu.ie3.util.quantities.PowerSystemUnits.METRE

class GeoUtilsTest extends Specification {

    @Shared
    GeoJsonReader geoJsonReader

    def setupSpec() {
        geoJsonReader = new GeoJsonReader()
    }

    def "Trying to instantiate the GeoUtils leads to an exception"() {
        when:
            new GeoUtils()

        then:
            def ex = thrown(IllegalStateException)
            ex.message == "Utility classes cannot be instantiated."
    }

    def "GeoUtils should build a line string with two exact equal geo coordinates correctly avoiding the known bug in jts geometry"() {
        given:
            def line = geoJsonReader.read(lineString) as LineString

        when:
            def safeLineString = GeoUtils.buildSafeLineString(line)
            def actualCoordinates = safeLineString.coordinates

        then:
            coordinates.length == actualCoordinates.length
            for (int cnt = 0; cnt < coordinates.length; cnt++) {
                coordinates[cnt] == actualCoordinates[cnt]
            }

        where:
            lineString                                                                                                                            | coordinates
            "{ \"type\": \"LineString\", \"coordinates\": [[7.411111, 51.49228],[7.411111, 51.49228]]}"                                           | [
                    new Coordinate(7.4111110000001, 51.4922800000001),
                    new Coordinate(7.411111, 51.49228)
            ] as Coordinate[]
            "{ \"type\": \"LineString\", \"coordinates\": [[7.411111, 51.49228],[7.411111, 51.49228],[7.411111, 51.49228],[7.411111, 51.49228]]}" | [
                    new Coordinate(7.4111110000001, 51.4922800000001),
                    new Coordinate(7.411111, 51.49228)
            ] as Coordinate[]
            "{ \"type\": \"LineString\", \"coordinates\": [[7.411111, 51.49228],[7.411111, 51.49228],[7.311111, 51.49228],[7.511111, 51.49228]]}" | [
                    new Coordinate(7.411111, 51.49228),
                    new Coordinate(7.311111, 51.49228),
                    new Coordinate(7.511111, 51.49228)
            ] as Coordinate[]
    }

    def "GeoUtils maintains the correct order of coordinates, when overhauling a given LineString"() {
        /* Remark: This test might even NOT fail, if the method is implemented incorrectly (utilizing a HashSet to
         * maintain uniqueness). For detailed explanation cf. comment in method's implementation. */
        given:
            def coordinates = [
                    new Coordinate(51.49292, 7.41197),
                    new Coordinate(51.49333, 7.41183),
                    new Coordinate(51.49341, 7.41189),
                    new Coordinate(51.49391, 7.41172),
                    new Coordinate(51.49404, 7.41279)
            ] as Coordinate[]
            def lineString = GeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString(coordinates)

        when:
            def actual = GeoUtils.buildSafeLineString(lineString).coordinates

        then:
            coordinates.length == actual.length
            for (int cnt = 0; cnt < coordinates.length; cnt++) {
                coordinates[cnt] == actual[cnt]
            }
    }

    def "GeoUtils should build a safe instance of a LineString between two provided points correctly"() {
        given:
            def pointA = GeoUtils.buildPoint(coordALat, coordALong)
            def pointB = GeoUtils.buildPoint(coordBLat, coordBLong)
            def resLineString = GeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString(
                    [
                            new Coordinate(resCoordALong, resCoordALat),
                            new Coordinate(coordBLong, coordBLat)
                    ] as Coordinate[]
            )

        expect:
            GeoUtils.buildSafeLineStringBetweenPoints(pointA, pointB) == resLineString
            // do not change or remove the following line, it is NOT equal to the line above in this case!
            GeoUtils.buildSafeLineStringBetweenPoints(pointA, pointB).equals(resLineString)

        where:
            coordALat | coordALong | coordBLat | coordBLong || resCoordALat      | resCoordALong
            1d        | 1d         | 1d        | 1d         || 1.0000000000001d  | 1.0000000000001d
            51.49228d | 7.411111d  | 51.49228d | 7.411111d  || 51.4922800000001d | 7.4111110000001d
            1d        | 1d         | 2d        | 2d         || 1d                | 1d

    }

    def "GeoUtils should only modify a provided Coordinate as least as possible"() {
        given:
            def coord = new Coordinate(1, 1, 0)

        expect:
            GeoUtils.buildSafeCoord(coord) == new Coordinate(1.0000000000001, 1.0000000000001, 1.0E-13)
    }

    def "GeoUtils should get the CoordinateDistances between a base point and a collection of other points correctly"() {
        given:
            def basePoint = GeoUtils.buildPoint(49d, 7d)
            def points = [
                    GeoUtils.buildPoint(50d, 7d),
                    GeoUtils.buildPoint(50d, 7.1d),
                    GeoUtils.buildPoint(52d, 9d),
                    GeoUtils.buildPoint(49d, 7.1d)
            ]
            def coordinateDistances = [
                    new CoordinateDistance(basePoint, points[3]),
                    new CoordinateDistance(basePoint, points[0]),
                    new CoordinateDistance(basePoint, points[1]),
                    new CoordinateDistance(basePoint, points[2])
            ]

        expect:
            GeoUtils.calcOrderedCoordinateDistances(basePoint, points) == coordinateDistances
    }

    def "GeoUtils should return all CoordinateDistances correctly"() {
        given:
        def basePoint = GeoUtils.buildPoint(50.5, 7d)
        def points = [
                GeoUtils.buildPoint(50d, 7d),
                GeoUtils.buildPoint(51d, 7d)
        ]

        when:
        def actual = GeoUtils.calcOrderedCoordinateDistances(basePoint, points)

        then:
        actual.size() == 2
    }

    def "GeoUtils should calculate haversine distance between two points correctly"() {
        given:
            Coordinate start = new Coordinate(-122.25311279296875, 37.87532764735112)
            Coordinate end = new Coordinate(-122.2537350654602, 37.87934174490509)
            ComparableQuantity<Length> tolerance = Quantities.getQuantity(1e-12d, METRE)
            ComparableQuantity<Length> expected = Quantities.getQuantity(450.18011568984845, METRE)

        when:
            ComparableQuantity<Length> actual = GeoUtils.calcHaversine(start, end)

        then:
            Math.abs(actual.subtract(expected).to(METRE).value.doubleValue()) < tolerance.value.doubleValue()
    }

    def "GeoUtils should calculate haversine distance of LineString correctly"() {
        given:
            def lineString = GeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString([
                    new Coordinate(11.13038d, 22.69962d, 0),
                    new Coordinate(28.14743d, 20.84247d, 0),
                    new Coordinate(12.04265d, 24.21942d, 0)
            ] as Coordinate[])

        when:
            ComparableQuantity<Length> y = GeoUtils.calcHaversine(lineString)

        then:
            QuantityUtil.isEquivalentAbs(y, Quantities.getQuantity(3463370, METRE), 10000)
            // Value from Google Maps, error range of +-10 km
    }

    def "GeoUtils calculates a convex hull correctly"() {
        given:
            def topLeft = new Coordinate(7, 50)
            def betweenTlTr = new Coordinate(7.5, 50)
            def topRight = new Coordinate(8, 50)
            def bottomRight = new Coordinate(8, 48)
            def betweenBlBr = new Coordinate(7.5, 48)
            def bottomLeft = new Coordinate(7, 48)
            def coordinates = [topLeft,
                               betweenTlTr,
                               topRight,
                               bottomRight,
                               betweenBlBr,
                               bottomLeft] as Set

            def cornerCoordinates =
                    [topLeft, topRight, bottomRight, bottomLeft] as List

        when:
            def convexHull = GeoUtils.buildConvexHull(coordinates)
            def hullCoordinates = convexHull.coordinates

        then:
            // contains all corner coordinates and filters out all colinear ones
            hullCoordinates.size() == 5
            (hullCoordinates as Set).containsAll(cornerCoordinates)
    }

    def "GeoUtils creates a Point correctly given coordinate in doubles"() {
        given:
            def coord = new Coordinate(7.468448342940863d, 51.49860455457335d)
            def expected = new GeometryFactory(new PrecisionModel(), 4326).createPoint(coord)

        when:
            def actual = GeoUtils.buildPoint(coord)

        then:
            actual == expected
    }

    def "GeoUtils creates a Point correctly given a coordinate"() {
        given:
            def lat = 51.49860455457335
            def lng = 7.468448342940863
            def expected = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(lng, lat))

        when:
            def actual = GeoUtils.buildPoint(lat, lng)

        then:
            actual == expected
    }

    def "GeoUtils creates a Polygon correctly"() {
        given:
            def coordinateA = new Coordinate(7.468448342940863, 51.49860455457335)
            def coordinateB = new Coordinate(7.521007845835815, 51.50450661471354)
            def coordinateC = new Coordinate(7.5598606548385545, 51.456498140367934)
            def coordinates = [coordinateA, coordinateB, coordinateC, coordinateA] as Coordinate[]

        when:
            def actual = GeoUtils.buildPolygon(coordinates)

        then:
            actual.coordinates == coordinates
    }

    def "GeoUtils does an equal area projection of a coordinate correctly"() {
        when:
            def actual = GeoUtils.equalAreaProjection(
                    new Coordinate(8.748497631269068, 51.72341137638795)
            )

        then:
            Math.abs(actual.x - 603277.0126920443d) < 1e-12d
            Math.abs(actual.y - 5757823.816510521d) < 1e-12d
    }

    def "GeoUtils reverses the equal area projection correctly"() {
        when:
            def original = new Coordinate(7.468448342940863d, 51.49860455457335d)
            def reversed = GeoUtils.reverseEqualAreaProjection(GeoUtils.equalAreaProjection(original))

        then:
            Math.abs(original.x - reversed.x) < 1e-12
            Math.abs(original.y - reversed.y) < 1e-12
    }

    def "GeoUtils creates a circle polygon correctly"() {
        given:
            def center = new Coordinate(7.40110716d, 52.02083574d)
            def radius = Quantities.getQuantity(50, METRE)
            def precision = 1e-8d

        when:
            def poly = GeoUtils.buildCirclePolygon(center, radius)
            def actualCoordinates = poly.coordinates

        then:
            actualCoordinates.size() == 361

            // rounded distance should be 50 meters
            actualCoordinates.toList().forEach({ point ->
                def distance = GeoUtils.calcHaversine(center.y, center.x, point.y, point.x).to(METRE).value.doubleValue()
                assert Math.abs(distance - radius.to(METRE).value.doubleValue()) < precision
            })
    }

    def "GeoUtils calculates an orthogonal projection correctly"() {
        given:
            def coordinateA = GeoUtils.buildCoordinate(50d, 7)
            def coordinateB = GeoUtils.buildCoordinate(50d, 8)
            def point = GeoUtils.buildCoordinate(49d, 7.5)
        when:
            def actual = GeoUtils.orthogonalProjection coordinateA, coordinateB, point
        then:
            Math.abs(actual.x - 7.5) < 1e-9
            Math.abs(actual.y - 50d) < 1e-9
    }

    def "GeoUtils calculates x-delta (longitude) correctly"() {
        given:
        Point coordinate = GeoUtils.buildPoint(50, 7)
        ComparableQuantity<Length> distanceX = GeoUtils.calcHaversine(50, 7, 50, 6)

        when:
        Envelope envelope = GeoUtils.calculateBoundingBox(coordinate, distanceX)

        then:
        envelope.minX == 6
        envelope.maxX == 8

        double yMin = 49.35721717797476
        double yMax = 50.64278282202524

        envelope.minY == yMin
        envelope.maxY == yMax
    }

    def "GeoUtils calculates y-delta (latitude) correctly"() {
        given:
        Point coordinate = GeoUtils.buildPoint(50, 7)
        ComparableQuantity<Length> distanceX = GeoUtils.calcHaversine(51, 7, 52, 7)

        when:
        Envelope envelope = GeoUtils.calculateBoundingBox(coordinate, distanceX)

        then:
        double xMin = 5.444248126340358
        double xMax = 8.555751873659641

        envelope.minX == xMin
        envelope.maxX == xMax

        envelope.minY == 49
        envelope.maxY == 51
    }

    def "GeoUtils creates a Point with explicit z-coordinate"() {
        given:
            def lat = 50.5
            def lng = 7.5
            def z = 120.5

        when:
            def point = GeoUtils.buildPoint(lat, lng, z)

        then:
            point.coordinate.x == lng
            point.coordinate.y == lat
            point.coordinate.z == z
    }

    def "GeoUtils creates a Coordinate with explicit z-coordinate"() {
        given:
            def lat = 50.5
            def lng = 7.5
            def z = 120.5

        when:
            def coord = GeoUtils.buildCoordinate(lat, lng, z)

        then:
            coord.x == lng
            coord.y == lat
            coord.z == z
    }

    def "GeoUtils sets default z=0.0 when building Point without z"() {
        given:
            def lat = 50.5
            def lng = 7.5

        when:
            def point = GeoUtils.buildPoint(lat, lng)

        then:
            point.coordinate.x == lng
            point.coordinate.y == lat
            point.coordinate.z == 0.0
    }

    def "GeoUtils sets default z=0.0 when building Coordinate without z"() {
        given:
            def lat = 50.5
            def lng = 7.5

        when:
            def coord = GeoUtils.buildCoordinate(lat, lng)

        then:
            coord.x == lng
            coord.y == lat
            coord.z == 0.0
    }

    def "Equal area projection discards z-coordinate and sets it to 0.0"() {
        given:
            def coordWithZ = new Coordinate(8.5, 51.5, 100.0)

        when:
            def projected = GeoUtils.equalAreaProjection(coordWithZ)

        then:
        projected.z == 0.0
    }

    def "Reverse equal area projection discards z-coordinate and sets it to 0.0"() {
        given:
            def projectedCoord = new Coordinate(600000.0, 5750000.0, 50.0)

        when:
            def reversed = GeoUtils.reverseEqualAreaProjection(projectedCoord)

        then:
            reversed.z == 0.0
    }

    def "Build circle polygon sets z=0.0 for all points regardless of center z"() {
        given:
            def center = new Coordinate(7.5, 50.5, 200.0)
            def radius = Quantities.getQuantity(1.0, METRE)

        when:
            def polygon = GeoUtils.buildCirclePolygon(center, radius)

        then:
            def coords = polygon.coordinates
            coords.every { it.z == 0.0 }
    }

    def "Haversine distance ignores z-coordinate"() {
        given:
            def coordA = new Coordinate(7.0, 50.0, 100.0)
            def coordB = new Coordinate(8.0, 51.0, 200.0)
            def coordANoZ = new Coordinate(7.0, 50.0, 0.0)
            def coordBNoZ = new Coordinate(8.0, 51.0, 0.0)

        when:
            def distanceWithZ = GeoUtils.calcHaversine(coordA, coordB)
            def distanceWithoutZ = GeoUtils.calcHaversine(coordANoZ, coordBNoZ)

        then:
            distanceWithZ == distanceWithoutZ
    }
}

