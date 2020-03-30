/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.interval

import spock.lang.Shared
import spock.lang.Specification

class ClosedIntervalTest extends Specification {
	@Shared
	ClosedInterval<Integer> dut

	def setupSpec() {
		dut = new ClosedInterval<>(1, 3)
	}

	def "Valid constructor test"() {
		when:
		ClosedInterval dut = new ClosedInterval<>(1, 3)

		then:
		dut.getLower() == 1
		dut.getUpper() == 3
	}

	def "Lower bound null test"() {
		when:
		new ClosedInterval<>(null, 3)

		then:
		NullPointerException exception = thrown(NullPointerException.class)
		exception.message == "Bound must not be null"
	}

	def "Upper bound null test"() {
		when:
		new ClosedInterval<>(1, null)

		then:
		NullPointerException exception = thrown(NullPointerException.class)
		exception.message == "Bound must not be null"
	}

	def "Twisted bounds test"() {
		when:
		new ClosedInterval<>(3, 1)

		then:
		IllegalArgumentException exception = thrown(IllegalArgumentException.class)
		exception.message == "Upper boundary may not be smaller than lower boundary."
	}

	def "Contains test"() {
		expect:
		dut.includes(value) == expected

		where:
		value | expected
		0 | false
		1 | true
		2 | true
		3 | true
		4 | false
	}
}
