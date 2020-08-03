/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import spock.lang.Unroll

import static edu.ie3.util.quantities.dep.PowerSystemUnits.DEGREE_GEOM
import static edu.ie3.util.quantities.dep.PowerSystemUnits.KILOWATT
import static edu.ie3.util.quantities.dep.PowerSystemUnits.MEGAWATT
import static edu.ie3.util.quantities.dep.PowerSystemUnits.KILOWATTHOUR_PER_SQUAREMETRE
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

/** @deprecated As of release 1.4, replaced by {@link QuantityUtilTestNew} */
@Deprecated
class QuantityUtilTest extends Specification {
	def "The QuantityUtil converts different quantities correctly to comparable quantities of same value and unit"() {
		when:
		def actual = QuantityUtil.makeComparable(input)

		then:
		actual instanceof ComparableQuantity
		actual.value == expected.value
		actual.unit == expected.unit

		where:
		input                                                     || expected
		Quantities.getQuantity(10d, KILOWATT)                     || Quantities.getQuantity(10d, KILOWATT) as ComparableQuantity<Power>
		Quantities.getQuantity(15d, DEGREE_GEOM)                  || Quantities.getQuantity(15d, DEGREE_GEOM) as ComparableQuantity<Angle>
		Quantities.getQuantity(20d, METRE_PER_SECOND)             || Quantities.getQuantity(20d, METRE_PER_SECOND) as ComparableQuantity<Speed>
		Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE) || Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE) as ComparableQuantity<Irradiation>
		Quantities.getQuantity(30d, METRE)                        || Quantities.getQuantity(30d, METRE) as ComparableQuantity<Length>
	}

	@Unroll
	def "The QuantityUtil calculates absolute considerably equal values correctly (a = #a, b = #b, expected result = #expected)"() {
		when:
		def actual = QuantityUtil.considerablyAbsEqual(a, b, 0.001)

		then:
		actual == expected

		where:
		a                                               | b                                        || expected
		Quantities.getQuantity(10d, KILOWATT)           | Quantities.getQuantity(10d, KILOWATT)    || true
		Quantities.getQuantity(10d, KILOWATT)           | Quantities.getQuantity(0.010, MEGAWATT)  || true
		Quantities.getQuantity(15.0000001, DEGREE_GEOM) | Quantities.getQuantity(15d, DEGREE_GEOM) || true
		Quantities.getQuantity(15.1, DEGREE_GEOM)       | Quantities.getQuantity(15d, DEGREE_GEOM) || false
	}

	@Unroll
	def "The QuantityUtil calculates relative considerably equal values correctly (a = #a, b = #b, expected result = #expected)"() {
		when:
		def actual = QuantityUtil.considerablyRelEqual(a, b, 0.1)

		then:
		actual == expected

		where:
		a                                               | b                                        || expected
		Quantities.getQuantity(10d, KILOWATT)           | Quantities.getQuantity(10d, KILOWATT)    || true
		Quantities.getQuantity(10d, KILOWATT)           | Quantities.getQuantity(0.010, MEGAWATT)  || true
		Quantities.getQuantity(15.0000001, DEGREE_GEOM) | Quantities.getQuantity(15d, DEGREE_GEOM) || true
		Quantities.getQuantity(15.9, DEGREE_GEOM)       | Quantities.getQuantity(14d, DEGREE_GEOM) || false
	}

	def "Comparing two angle quantities absolutely shows considerably different values"() {
		when:
		def a = Quantities.getQuantity(1d, DEGREE_GEOM)
		def b = Quantities.getQuantity(1.1d, DEGREE_GEOM)

		then:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	def "Comparing two angle quantities absolutely shows considerably equal values"() {
		when:
		def a = Quantities.getQuantity(1d, DEGREE_GEOM)
		def b = Quantities.getQuantity(1.001d, DEGREE_GEOM)

		then:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	def "Comparing two angle quantities absolutely shows considerably different values close to 180 degree"() {
		expect:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a 											| b
		Quantities.getQuantity(175d, DEGREE_GEOM) 	| Quantities.getQuantity(-175d, DEGREE_GEOM)
		Quantities.getQuantity(-175d, DEGREE_GEOM) 	| Quantities.getQuantity(175d, DEGREE_GEOM)
		Quantities.getQuantity(175d, DEGREE_GEOM) 	| Quantities.getQuantity(-185.1, DEGREE_GEOM)
		Quantities.getQuantity(185d, DEGREE_GEOM) 	| Quantities.getQuantity(-174.9, DEGREE_GEOM)
	}

	def "Comparing two angle quantities absolutely shows considerably equal values close to 180 degree"() {
		expect:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a                                          | b
		Quantities.getQuantity(175d, DEGREE_GEOM)  | Quantities.getQuantity(-185d, DEGREE_GEOM)
		Quantities.getQuantity(-185d, DEGREE_GEOM) | Quantities.getQuantity(175d, DEGREE_GEOM)
		Quantities.getQuantity(185d, DEGREE_GEOM)  | Quantities.getQuantity(-175d, DEGREE_GEOM)
		Quantities.getQuantity(-175d, DEGREE_GEOM) | Quantities.getQuantity(185d, DEGREE_GEOM)
		Quantities.getQuantity(185d, DEGREE_GEOM)  | Quantities.getQuantity(-174.9995d, DEGREE_GEOM)
	}
}
