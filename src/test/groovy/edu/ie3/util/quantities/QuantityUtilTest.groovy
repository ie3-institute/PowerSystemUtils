/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import edu.ie3.util.quantities.dep.interfaces.Irradiation
import spock.lang.Specification
import spock.lang.Unroll
import tec.uom.se.ComparableQuantity
import tec.uom.se.quantity.Quantities

import javax.measure.quantity.Angle
import javax.measure.quantity.Length
import javax.measure.quantity.Power
import javax.measure.quantity.Speed

import static edu.ie3.util.quantities.PowerSystemUnits.DEGREE_GEOM
import static edu.ie3.util.quantities.dep.PowerSystemUnits.*
import static edu.ie3.util.quantities.dep.PowerSystemUnits.DEGREE_GEOM as DEGREE_GEOM_DEP
import static tec.uom.se.unit.Units.METRE
import static tec.uom.se.unit.Units.METRE_PER_SECOND

class QuantityUtilTest extends Specification {
	/** @deprecated As of release 1.4   */
	@Deprecated
	def "The QuantityUtil converts different quantities correctly to comparable quantities of same value and unit (old package)"() {
		when:
		def actual = QuantityUtil.makeComparable(input)

		then:
		actual instanceof ComparableQuantity
		actual.value == expected.value
		actual.unit == expected.unit

		where:
		input                                                     || expected
		Quantities.getQuantity(10d, KILOWATT)                     || Quantities.getQuantity(10d, KILOWATT) as ComparableQuantity<Power>
		Quantities.getQuantity(15d, DEGREE_GEOM_DEP)              || Quantities.getQuantity(15d, DEGREE_GEOM_DEP) as ComparableQuantity<Angle>
		Quantities.getQuantity(20d, METRE_PER_SECOND)             || Quantities.getQuantity(20d, METRE_PER_SECOND) as ComparableQuantity<Speed>
		Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE) || Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE) as ComparableQuantity<Irradiation>
		Quantities.getQuantity(30d, METRE)                        || Quantities.getQuantity(30d, METRE) as ComparableQuantity<Length>
	}

	def "The QuantityUtil converts different quantities correctly to comparable quantities of same value and unit"() {
		when:
		def actual = QuantityUtil.asComparable(input)

		then:
		actual instanceof tech.units.indriya.ComparableQuantity
		actual.value == expected.value
		actual.unit == expected.unit

		where:
		input                                                     							  || expected
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)                     || tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)
		tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM)                  || tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(20d, METRE_PER_SECOND)             || tech.units.indriya.quantity.Quantities.getQuantity(20d, METRE_PER_SECOND)
		tech.units.indriya.quantity.Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE) || tech.units.indriya.quantity.Quantities.getQuantity(25d, KILOWATTHOUR_PER_SQUAREMETRE)
		tech.units.indriya.quantity.Quantities.getQuantity(30d, METRE)                        || tech.units.indriya.quantity.Quantities.getQuantity(30d, METRE)
	}

	@Unroll
	def "The QuantityUtil calculates absolute considerably equal values correctly (a = #a, b = #b, expected result = #expected)"() {
		when:
		def actual = QuantityUtil.considerablyAbsEqual(a, b, 0.001)

		then:
		actual == expected

		where:
		a                                               	| b                                            || expected
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(10d, KILOWATT)        || true
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(0.010, MEGAWATT)      || true
		Quantities.getQuantity(15.0000001, DEGREE_GEOM_DEP) | Quantities.getQuantity(15d, DEGREE_GEOM_DEP) || true
		Quantities.getQuantity(15.1, DEGREE_GEOM_DEP)       | Quantities.getQuantity(15d, DEGREE_GEOM_DEP) || false
	}

	/** @deprecated As of release 1.4 */
	@Deprecated
	@Unroll
	def "The QuantityUtil calculates absolute considerably equal values correctly (a = #a, b = #b, expected result = #expected) (old package)"() {
		when:
		def actual = QuantityUtil.considerablyAbsEqual(a, b, 0.001)

		then:
		actual == expected

		where:
		a                                               							| b                                        							   || expected
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)           | tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)    || true
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)           | tech.units.indriya.quantity.Quantities.getQuantity(0.010, MEGAWATT)  || true
		tech.units.indriya.quantity.Quantities.getQuantity(15.0000001, DEGREE_GEOM) | tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM) || true
		tech.units.indriya.quantity.Quantities.getQuantity(15.1, DEGREE_GEOM)       | tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM) || false
	}

	/** @deprecated As of release 1.4 */
	@Deprecated
	@Unroll
	def "The QuantityUtil calculates relative considerably equal values correctly (a = #a, b = #b, expected result = #expected) (old package)"() {
		when:
		def actual = QuantityUtil.considerablyRelEqual(a, b, 0.1)

		then:
		actual == expected

		where:
		a                                               	| b                                            || expected
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(10d, KILOWATT)        || true
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(0.010, MEGAWATT)      || true
		Quantities.getQuantity(15.0000001, DEGREE_GEOM_DEP) | Quantities.getQuantity(15d, DEGREE_GEOM_DEP) || true
		Quantities.getQuantity(15.9, DEGREE_GEOM_DEP)       | Quantities.getQuantity(14d, DEGREE_GEOM_DEP) || false
	}

	@Unroll
	def "The QuantityUtil calculates relative considerably equal values correctly (a = #a, b = #b, expected result = #expected)"() {
		when:
		def actual = QuantityUtil.considerablyRelEqual(a, b, 0.1)

		then:
		actual == expected

		where:
		a                                               							| b                                        							   || expected
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)           | tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)    || true
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)           | tech.units.indriya.quantity.Quantities.getQuantity(0.010, MEGAWATT)  || true
		tech.units.indriya.quantity.Quantities.getQuantity(15.0000001, DEGREE_GEOM) | tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM) || true
		tech.units.indriya.quantity.Quantities.getQuantity(15.9, DEGREE_GEOM)       | tech.units.indriya.quantity.Quantities.getQuantity(14d, DEGREE_GEOM) || false
	}

	/** @deprecated As of release 1.4 */
	@Deprecated
	def "Comparing two angle quantities absolutely shows considerably different values (old package)"() {
		when:
		def a = Quantities.getQuantity(1d, DEGREE_GEOM_DEP)
		def b = Quantities.getQuantity(1.1d, DEGREE_GEOM_DEP)

		then:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	def "Comparing two angle quantities absolutely shows considerably different values"() {
		when:
		def a = tech.units.indriya.quantity.Quantities.getQuantity(1d, DEGREE_GEOM)
		def b = tech.units.indriya.quantity.Quantities.getQuantity(1.1d, DEGREE_GEOM)

		then:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	/** @deprecated As of release 1.4 */
	@Deprecated
	def "Comparing two angle quantities absolutely shows considerably equal values (old package)"() {
		when:
		def a = Quantities.getQuantity(1d, DEGREE_GEOM_DEP)
		def b = Quantities.getQuantity(1.001d, DEGREE_GEOM_DEP)

		then:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	def "Comparing two angle quantities absolutely shows considerably equal values"() {
		when:
		def a = tech.units.indriya.quantity.Quantities.getQuantity(1d, DEGREE_GEOM)
		def b = tech.units.indriya.quantity.Quantities.getQuantity(1.001d, DEGREE_GEOM)

		then:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	/** @deprecated As of release 1.4 */
	@Deprecated
	def "Comparing two angle quantities absolutely shows considerably different values close to 180 degree (old package)"() {
		expect:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a 												| b
		Quantities.getQuantity(175d, DEGREE_GEOM_DEP) 	| Quantities.getQuantity(-175d, DEGREE_GEOM_DEP)
		Quantities.getQuantity(-175d, DEGREE_GEOM_DEP) 	| Quantities.getQuantity(175d, DEGREE_GEOM_DEP)
		Quantities.getQuantity(175d, DEGREE_GEOM_DEP) 	| Quantities.getQuantity(-185.1, DEGREE_GEOM_DEP)
		Quantities.getQuantity(185d, DEGREE_GEOM_DEP) 	| Quantities.getQuantity(-174.9, DEGREE_GEOM_DEP)
	}

	def "Comparing two angle quantities absolutely shows considerably different values close to 180 degree"() {
		expect:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a 											| b
		tech.units.indriya.quantity.Quantities.getQuantity(175d, DEGREE_GEOM) 	| tech.units.indriya.quantity.Quantities.getQuantity(-175d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(-175d, DEGREE_GEOM) 	| tech.units.indriya.quantity.Quantities.getQuantity(175d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(175d, DEGREE_GEOM) 	| tech.units.indriya.quantity.Quantities.getQuantity(-185.1, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(185d, DEGREE_GEOM) 	| tech.units.indriya.quantity.Quantities.getQuantity(-174.9, DEGREE_GEOM)
	}

	/** @deprecated As of release 1.4 */
	@Deprecated
	def "Comparing two angle quantities absolutely shows considerably equal values close to 180 degree (old package)"() {
		expect:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a                                          | b
		Quantities.getQuantity(175d, DEGREE_GEOM_DEP)  | Quantities.getQuantity(-185d, DEGREE_GEOM_DEP)
		Quantities.getQuantity(-185d, DEGREE_GEOM_DEP) | Quantities.getQuantity(175d, DEGREE_GEOM_DEP)
		Quantities.getQuantity(185d, DEGREE_GEOM_DEP)  | Quantities.getQuantity(-175d, DEGREE_GEOM_DEP)
		Quantities.getQuantity(-175d, DEGREE_GEOM_DEP) | Quantities.getQuantity(185d, DEGREE_GEOM_DEP)
		Quantities.getQuantity(185d, DEGREE_GEOM_DEP)  | Quantities.getQuantity(-174.9995d, DEGREE_GEOM_DEP)
	}

	def "Comparing two angle quantities absolutely shows considerably equal values close to 180 degree"() {
		expect:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a                                                                      | b
		tech.units.indriya.quantity.Quantities.getQuantity(175d, DEGREE_GEOM)  | tech.units.indriya.quantity.Quantities.getQuantity(-185d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(-185d, DEGREE_GEOM) | tech.units.indriya.quantity.Quantities.getQuantity(175d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(185d, DEGREE_GEOM)  | tech.units.indriya.quantity.Quantities.getQuantity(-175d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(-175d, DEGREE_GEOM) | tech.units.indriya.quantity.Quantities.getQuantity(185d, DEGREE_GEOM)
		tech.units.indriya.quantity.Quantities.getQuantity(185d, DEGREE_GEOM)  | tech.units.indriya.quantity.Quantities.getQuantity(-174.9995d, DEGREE_GEOM)
	}

	@Unroll
	def "The QuantityUtil provides a correct equality check"() {
		when:
		def actual = QuantityUtil.equals(a, b)

		then:
		actual == expected

		where:
		a                                                                     | b                                                                    || expected
		null                                                                  | null                                                                 || true
		null                                                                  | tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)    || false
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)     | null                                                                 || false
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)     | tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)    || true
		tech.units.indriya.quantity.Quantities.getQuantity(10d, KILOWATT)     | tech.units.indriya.quantity.Quantities.getQuantity(0.010, MEGAWATT)  || true
		tech.units.indriya.quantity.Quantities.getQuantity(15i, DEGREE_GEOM)  | tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM) || true
		tech.units.indriya.quantity.Quantities.getQuantity(15.1, DEGREE_GEOM) | tech.units.indriya.quantity.Quantities.getQuantity(15d, DEGREE_GEOM) || false
	}

}
