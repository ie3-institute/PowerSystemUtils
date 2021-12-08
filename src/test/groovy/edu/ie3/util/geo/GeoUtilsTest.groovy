/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.geo

import edu.ie3.util.quantities.PowerSystemUnits
import edu.ie3.util.quantities.QuantityUtil
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.io.geojson.GeoJsonReader
import spock.lang.Shared

import static edu.ie3.util.quantities.PowerSystemUnits.METRE

import net.morbz.osmonaut.geometry.Polygon
import net.morbz.osmonaut.osm.LatLon
import spock.lang.Specification

import javax.measure.Quantity
import javax.measure.quantity.Length
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities



class GeoUtilsTest extends Specification {

	@Shared
	GeoJsonReader geoJsonReader

	def setupSpec() {
		geoJsonReader = new GeoJsonReader()
	}

	def "Test haversine (distance between two points given lat/lon)"() {
		given:
		LatLon start = new LatLon(37.87532764735112, -122.25311279296875)
		LatLon end = new LatLon(37.87934174490509, -122.2537350654602)
		ComparableQuantity<Length> tolerance = Quantities.getQuantity(1d, METRE)
		ComparableQuantity<Length> expected = Quantities.getQuantity(450.18011568984845, METRE)

		when:
		ComparableQuantity<Length> actual = DeprecatedGeoUtils.calcHaversine(start.lat, start.lon, end.lat, end.lon)

		then:
		Math.abs(actual.subtract(expected).to(METRE).value.doubleValue()) < tolerance	.value.doubleValue()
	}

	def "Total length of LineString is correctly calculated"() {
		given:
		def lineString = DeprecatedGeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString([
			new Coordinate(22.69962d, 11.13038d, 0),
			new Coordinate(20.84247d, 28.14743d, 0),
			new Coordinate(24.21942d, 12.04265d, 0)
		] as Coordinate[])

		when:
		ComparableQuantity<Length> y = DeprecatedGeoUtils.totalLengthOfLineString(lineString)

		then:
		QuantityUtil.isEquivalentAbs(y, Quantities.getQuantity(3463.37, PowerSystemUnits.KILOMETRE), 10)
		// Value from Google Maps, error range of +-10 km
	}

	def "The GridAndGeoUtils should get the CoordinateDistances between a base point and a collection of other points correctly"() {
		given:
		def basePoint = DeprecatedGeoUtils.xyToPoint(49d, 7d)
		def points = [
			DeprecatedGeoUtils.xyToPoint(50d, 7d),
			DeprecatedGeoUtils.xyToPoint(50d, 7.1d),
			DeprecatedGeoUtils.xyToPoint(49d, 7.1d),
			DeprecatedGeoUtils.xyToPoint(52d, 9d)
		]
		def coordinateDistances = [
			new DeprecatedCoordinateDistance(basePoint, points[0]),
			new DeprecatedCoordinateDistance(basePoint, points[1]),
			new DeprecatedCoordinateDistance(basePoint, points[2]),
			new DeprecatedCoordinateDistance(basePoint, points[3])
		]
		expect:
		DeprecatedGeoUtils.getCoordinateDistances(basePoint, points) == new TreeSet(coordinateDistances)
	}

	def "The GridAndGeoUtils should build a line string with two exact equal geo coordinates correctly avoiding the known bug in jts geometry"() {
		given:
		def line = geoJsonReader.read(lineString) as LineString

		when:
		def safeLineString = DeprecatedGeoUtils.buildSafeLineString(line)
		def actualCoordinates = safeLineString.coordinates

		then:
		coordinates.length == actualCoordinates.length
		for (int cnt = 0; cnt < coordinates.length; cnt++) {
			coordinates[cnt] == actualCoordinates[cnt]
		}
		from
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

	def "The GridAngGeoUtils maintain the correct order of coordinates, when overhauling a given LineString"() {
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
		def lineString = DeprecatedGeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString(coordinates)

		when:
		def actual = DeprecatedGeoUtils.buildSafeLineString(lineString).coordinates

		then:
		coordinates.length == actual.length
		for (int cnt = 0; cnt < coordinates.length; cnt++) {
			coordinates[cnt] == actual[cnt]
		}
	}

	def "The GridAndGeoUtils should only modify a provided Coordinate as least as possible"() {
		given:
		def coord = new Coordinate(1, 1, 0)

		expect:
		DeprecatedGeoUtils.buildSafeCoord(coord) == new Coordinate(1.0000000000001, 1.0000000000001, 1.0E-13)
	}

	def "The GridAndGeoUtils should build a safe instance of a LineString between two provided coordinates correctly"() {

		expect:
		DeprecatedGeoUtils.buildSafeLineStringBetweenCoords(coordA, coordB) == resLineString
		// do not change or remove the following line, it is NOT equal to the line above in this case!
		DeprecatedGeoUtils.buildSafeLineStringBetweenCoords(coordA, coordB).equals(resLineString)

		where:
		coordA                  | coordB                  || resLineString
		new Coordinate(1, 1, 0) | new Coordinate(1, 1, 0) || DeprecatedGeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString([
			new Coordinate(1.0000000000001, 1.0000000000001, 1.0E-13),
			new Coordinate(1, 1, 0)
		] as Coordinate[])
		new Coordinate(1, 1, 0) | new Coordinate(2, 2, 0) || DeprecatedGeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString([
			new Coordinate(1, 1, 0),
			new Coordinate(2, 2, 0)
		] as Coordinate[])

	}

	def "Test radius with circle as polygon"() {
		given:
		LatLon center = new LatLon(52.02083574, 7.40110716)
		Quantity radius = Quantities.getQuantity(50, METRE)

		when:
		Polygon poly = DeprecatedGeoUtils.radiusWithCircleAsPolygon(center, radius)
		List<LatLon> circlePoints = poly.getCoords()

		then:
		// polygon should contain a center that is the provided center
		Math.round(poly.center.lat * 100000000) / 100000000 == Math.round(center.lat * 100000000) / 100000000
		Math.round(poly.center.lon * 100000000) / 100000000 == Math.round(center.lon * 100000000) / 100000000

		// number of expected circle points
		circlePoints.size() == 361
		// rounded distance should be 50 meters
		circlePoints.forEach({ point ->
			Double distance = DeprecatedGeoUtils.calcHaversine(center.lat, center.lon, point.lat, point.lon).to(METRE).value.doubleValue()
			Math.round(distance) == 50
		})

	}
}
