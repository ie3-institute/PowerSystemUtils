/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.geo

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
}
