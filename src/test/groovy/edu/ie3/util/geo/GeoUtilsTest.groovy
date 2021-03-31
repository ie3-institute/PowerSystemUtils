/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.geo

import edu.ie3.util.quantities.PowerSystemUnits
import edu.ie3.util.quantities.QuantityUtil
import org.locationtech.jts.geom.Coordinate

import static edu.ie3.util.quantities.PowerSystemUnits.METRE

import net.morbz.osmonaut.geometry.Polygon
import net.morbz.osmonaut.osm.LatLon
import spock.lang.Specification

import javax.measure.Quantity
import javax.measure.quantity.Length
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities



class GeoUtilsTest extends Specification {

	def "Test haversine (distance between two points given lat/lon)"() {
		given:
		LatLon start = new LatLon(37.87532764735112, -122.25311279296875)
		LatLon end = new LatLon(37.87934174490509, -122.2537350654602)
		ComparableQuantity<Length> tolerance = Quantities.getQuantity(1d, METRE)
		ComparableQuantity<Length> expected = Quantities.getQuantity(450.18011568984845, METRE)

		when:
		ComparableQuantity<Length> actual = GeoUtils.calcHaversine(start.lat, start.lon, end.lat, end.lon)

		then:
		Math.abs(actual.subtract(expected).to(METRE).value.doubleValue()) < tolerance.value.doubleValue()
	}

	def "Total length of LineString is correctly calculated"() {
		given:
		def lineString = GeoUtils.DEFAULT_GEOMETRY_FACTORY.createLineString([
			new Coordinate(22.69962d, 11.13038d, 0),
			new Coordinate(20.84247d, 28.14743d, 0),
			new Coordinate(24.21942d, 12.04265d, 0)] as Coordinate[])

		when:
		ComparableQuantity<Length> y = GeoUtils.totalLengthOfLineString(lineString)

		then:
		QuantityUtil.isEquivalentAbs(y, Quantities.getQuantity(3463.37, PowerSystemUnits.KILOMETRE), 10)
		// Value from Google Maps, error range of +-10 km
	}

	def "Test radius with circle as polygon"() {
		given:
		LatLon center = new LatLon(52.02083574, 7.40110716)
		Quantity radius = Quantities.getQuantity(50, METRE)

		when:
		Polygon poly = GeoUtils.radiusWithCircleAsPolygon(center, radius)
		List<LatLon> circlePoints = poly.getCoords()

		then:
		// polygon should contain a center that is the provided center
		Math.round(poly.center.lat * 100000000) / 100000000 == Math.round(center.lat * 100000000) / 100000000
		Math.round(poly.center.lon * 100000000) / 100000000 == Math.round(center.lon * 100000000) / 100000000

		// number of expected circle points
		circlePoints.size() == 361
		// rounded distance should be 50 meters
		circlePoints.forEach({ point ->
			Double distance = GeoUtils.calcHaversine(center.lat, center.lon, point.lat, point.lon).to(METRE).value.doubleValue()
			Math.round(distance) == 50
		})

	}
}
