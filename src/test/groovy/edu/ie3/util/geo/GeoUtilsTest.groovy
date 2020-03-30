/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.geo

import edu.ie3.util.quantities.PowerSystemUnits
import net.morbz.osmonaut.geometry.Polygon
import net.morbz.osmonaut.osm.LatLon
import spock.lang.Specification
import tec.uom.se.quantity.Quantities
import tec.uom.se.unit.Units

import javax.measure.Quantity


class GeoUtilsTest extends Specification {

	def "Test haversine (distance between two points given lat/lon)"() {
		given:
		LatLon start = new LatLon(37.87532764735112, -122.25311279296875)
		LatLon end = new LatLon(37.87934174490509, -122.2537350654602)

		expect:
		GeoUtils.haversine(start.lat, start.lon, end.lat, end.lon).getValue().doubleValue() ==
				Quantities.getQuantity(449.676373690315, Units.METRE)
				.to(PowerSystemUnits.KILOMETRE).getValue().doubleValue()
	}

	def "Test radius with circle as polygon"() {
		given:
		LatLon center = new LatLon(52.02083574, 7.40110716)
		Quantity radius = Quantities.getQuantity(50, Units.METRE)

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
			Double distance = GeoUtils.haversine(center.lat, center.lon, point.lat, point.lon).to(Units.METRE).getValue().doubleValue()
			Math.round(distance) == 50
		})

	}
}
