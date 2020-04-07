/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import static edu.ie3.util.quantities.PowerSystemUnits.DEGREE_GEOM
import static edu.ie3.util.quantities.PowerSystemUnits.KILOWATT
import static edu.ie3.util.quantities.PowerSystemUnits.KILOWATTHOUR_PER_SQUAREMETRE
import static tec.uom.se.unit.Units.METRE
import static tec.uom.se.unit.Units.METRE_PER_SECOND

import edu.ie3.util.quantities.interfaces.Irradiation

import javax.measure.quantity.Angle
import javax.measure.quantity.Length
import javax.measure.quantity.Power
import javax.measure.quantity.Speed

import spock.lang.Specification
import tec.uom.se.ComparableQuantity
import tec.uom.se.quantity.Quantities

class QuantityUtilTest extends Specification {
	def "The QuantityUtil converts different quantities correctly to comparable quantities of same value and unit"() {
		when:
		def actual = QuantityUtil.makeComparable(input)

		then:
		actual instanceof ComparableQuantity
		actual.value == expected.value
		actual.unit == expected.unit

		where:
		input                                                       || expected
		Quantities.getQuantity(10d, KILOWATT)                       || Quantities.getQuantity(10d, KILOWATT) as ComparableQuantity<Power>
		Quantities.getQuantity(15d, DEGREE_GEOM)                    || Quantities.getQuantity(15d, DEGREE_GEOM) as ComparableQuantity<Angle>
		Quantities.getQuantity(20d, METRE_PER_SECOND)               || Quantities.getQuantity(20d, METRE_PER_SECOND) as ComparableQuantity<Speed>
		Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE)   || Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE) as ComparableQuantity<Irradiation>
		Quantities.getQuantity(30d, METRE)                          || Quantities.getQuantity(30d, METRE) as ComparableQuantity<Length>
	}
}
