/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import static edu.ie3.util.quantities.PowerSystemUnits.*
import edu.ie3.util.quantities.interfaces.Irradiation
import spock.lang.Unroll
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities

import javax.measure.MetricPrefix

import javax.measure.quantity.Angle
import javax.measure.quantity.Length
import javax.measure.quantity.Power
import javax.measure.quantity.Speed

import spock.lang.Specification

class QuantityUtilTest extends Specification {

	def "The QuantityUtil converts different quantities correctly to comparable quantities of same value and unit"() {
		when:
		def actual = QuantityUtil.asComparable(input)

		then:
		actual instanceof ComparableQuantity
		actual.value == expected.value
		actual.unit == expected.unit

		where:
		input                                                     							  || expected
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
		a                                               	| b                                            	|| expected
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(10d, KILOWATT)        	|| true
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(0.010, MEGAWATT)      	|| true
		Quantities.getQuantity(15.0000001, DEGREE_GEOM) 	| Quantities.getQuantity(15d, DEGREE_GEOM) 		|| true
		Quantities.getQuantity(15.1, DEGREE_GEOM)       	| Quantities.getQuantity(15d, DEGREE_GEOM) 		|| false
	}

	@Unroll
	def "The QuantityUtil calculates relative considerably equal values correctly (a = #a, b = #b, expected result = #expected) (old package)"() {
		when:
		def actual = QuantityUtil.considerablyRelEqual(a, b, 0.1)

		then:
		actual == expected

		where:
		a                                               	| b                                            	|| expected
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(10d, KILOWATT)        	|| true
		Quantities.getQuantity(10d, KILOWATT)           	| Quantities.getQuantity(0.010, MEGAWATT)      	|| true
		Quantities.getQuantity(15.0000001, DEGREE_GEOM) 	| Quantities.getQuantity(15d, DEGREE_GEOM) 		|| true
		Quantities.getQuantity(15.9, DEGREE_GEOM)       	| Quantities.getQuantity(14d, DEGREE_GEOM) 		|| false
	}

	def "Comparing two angle quantities absolutely shows considerably different values (old package)"() {
		when:
		def a = Quantities.getQuantity(1d, DEGREE_GEOM)
		def b = Quantities.getQuantity(1.1d, DEGREE_GEOM)

		then:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}

	def "Comparing two angle quantities absolutely shows considerably equal values (old package)"() {
		when:
		def a = Quantities.getQuantity(1d, DEGREE_GEOM)
		def b = Quantities.getQuantity(1.001d, DEGREE_GEOM)

		then:
		QuantityUtil.considerablyEqualAngle(a, b, 1e-3)
	}


	def "Comparing two angle quantities absolutely shows considerably different values close to 180 degree (old package)"() {
		expect:
		!QuantityUtil.considerablyEqualAngle(a, b, 1e-3)

		where:
		a 												| b
		Quantities.getQuantity(175d, DEGREE_GEOM) 	| Quantities.getQuantity(-175d, DEGREE_GEOM)
		Quantities.getQuantity(-175d, DEGREE_GEOM) 	| Quantities.getQuantity(175d, DEGREE_GEOM)
		Quantities.getQuantity(175d, DEGREE_GEOM) 	| Quantities.getQuantity(-185.1, DEGREE_GEOM)
		Quantities.getQuantity(185d, DEGREE_GEOM) 	| Quantities.getQuantity(-174.9, DEGREE_GEOM)
	}

	def "Comparing two angle quantities absolutely shows considerably equal values close to 180 degree (old package)"() {
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


	def "A Quantity will only be evaluated as empty if it is an EmptyQuantity" () {
		when:
		quantity

		then:
		QuantityUtil.quantityIsEmpty(quantity) == expectedResult

		where:
		quantity                                             || expectedResult
		null                                                 || false
		EmptyQuantity.of(METRE)                              || true
		Quantities.getQuantity(17.1, METRE) 				 || false
		Quantities.getQuantity(0, METRE)    				 || false
	}


	def "Comparing two quantities for equality behaves as expected" () {
		when:
		quantityA
		quantityB

		then:
		QuantityUtil.isTheSameConsideringEmpty(quantityA, quantityB) == expectedResult
		QuantityUtil.isTheSameConsideringEmpty(quantityB, quantityA) == expectedResult

		where:
		quantityA                                                 | quantityB                            || expectedResult
		Quantities.getQuantity(1000, METRE)                       | Quantities.getQuantity(1000, METRE)  || true
		Quantities.getQuantity(1000d, METRE)                      | Quantities.getQuantity(1000, METRE)  || true
		Quantities.getQuantity(1, MetricPrefix.KILO(METRE))       | Quantities.getQuantity(1000, METRE)  || false
		Quantities.getQuantity(1d, MetricPrefix.KILO(METRE)) 	  | Quantities.getQuantity(1000, METRE)  || false
		Quantities.getQuantity(1000, METRE)                       | EmptyQuantity.of(METRE)              || false
		Quantities.getQuantity(1000d, METRE)                      | EmptyQuantity.of(METRE)              || false
		EmptyQuantity.of(METRE)                                   | Quantities.getQuantity(1000d, METRE) || false
		EmptyQuantity.of(METRE)                                   | EmptyQuantity.of(METRE)              || true
	}


	def "Comparing two quantities for equivalence behaves as expected" () {
		when:
		quantityA
		quantityB

		then:
		QuantityUtil.isEquivalentConsideringEmpty(quantityA, quantityB) == expectedResult
		QuantityUtil.isEquivalentConsideringEmpty(quantityB, quantityA) == expectedResult

		where:
		quantityA                                   			| quantityB                           		|| expectedResult
		Quantities.getQuantity(1000, METRE)         			| Quantities.getQuantity(1000, METRE) 		|| true
		Quantities.getQuantity(1000d, METRE)        			| Quantities.getQuantity(1000, METRE) 		|| true
		Quantities.getQuantity(1, MetricPrefix.KILO(METRE)) 	| Quantities.getQuantity(1000, METRE) 		|| true
		Quantities.getQuantity(1d, MetricPrefix.KILO(METRE))	| Quantities.getQuantity(1000, METRE) 		|| true
		Quantities.getQuantity(1000, METRE)        				| EmptyQuantity.of(METRE) 					|| false
		Quantities.getQuantity(1000d, METRE)        			| EmptyQuantity.of(METRE) 					|| false
		EmptyQuantity.of(METRE)        							| Quantities.getQuantity(1000d, METRE) 		|| false
		EmptyQuantity.of(METRE)        							| EmptyQuantity.of(METRE) 					|| true
	}
}
