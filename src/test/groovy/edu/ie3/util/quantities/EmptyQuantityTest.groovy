/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import edu.ie3.util.exceptions.EmptyQuantityException
import spock.lang.Specification
import tech.units.indriya.quantity.Quantities

class EmptyQuantityTest extends Specification {

	def "An EmptyQuantity can be initialized for a specific Unit and has 'null' as value"() {
		when:
		emptyQuantity

		then:
		emptyQuantity != null
		emptyQuantity.unit == expectedUnit
		emptyQuantity.value == null

		where:
		emptyQuantity                            || expectedUnit
		EmptyQuantity.of(PowerSystemUnits.METRE) || PowerSystemUnits.METRE
		EmptyQuantity.of(PowerSystemUnits.WATT)  || PowerSystemUnits.WATT
	}

	def "EmptyQuantity is not equal to a regular Quantity "() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)

		nullMetreQuantity.compareTo(filledMetreQuantity)

		then:
		def thrown = thrown(EmptyQuantityException)
		thrown.message == "An empty quantity cannot be compared against an actual quantity (17.1 m)."
	}

	def "regular Quantity is not equal to a regular Quantity EmptyQuantity"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)

		filledMetreQuantity.compareTo(nullMetreQuantity)

		then:
		def thrown = thrown(NullPointerException)
		thrown.message == "Cannot invoke \"Object.getClass()\" because \"number\" is null"
	}

	def "EmptyQuantity is never equivalent or equal to a regular Quantity "() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)

		then:
		!nullMetreQuantity.isEquivalentTo(filledMetreQuantity)
	}

	def "EmptyQuantity is always equivalent and equal to another EmptyQuantity "() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def nullWattQuantity = EmptyQuantity.of(PowerSystemUnits.WATT)

		then:
		nullMetreQuantity.isEquivalentTo(nullWattQuantity)
		//no, this cannot be replaced with an operator, because then it routes over AbstractQuantity and uses that equals method first
		nullMetreQuantity.equals(nullWattQuantity)
	}

	//sadly, we have no influence here, so we can't use a custom exception message
	def "If a regular Quantity is compared to an EmptyQuantity, a NullPointer is thrown"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		filledMetreQuantity.isEquivalentTo(nullMetreQuantity)

		then:
		thrown NullPointerException
	}

	def "The add operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.add(filledMetreQuantity)

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The subtract operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.subtract(filledMetreQuantity)

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The divide by Quantity operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.divide(filledMetreQuantity)

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The divide by Number operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def divisor = 17.1
		nullMetreQuantity.divide(divisor)

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The multiply by Quantity operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity * filledMetreQuantity

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The multiply by Number operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		def multiplier = 17.1
		nullMetreQuantity * multiplier

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The inverse operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		nullMetreQuantity.inverse()

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

	def "The negate operation on an EmptyQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = EmptyQuantity.of(PowerSystemUnits.METRE)
		nullMetreQuantity.negate()

		then:
		def exception = thrown(EmptyQuantityException)
		exception.message == EmptyQuantity.EXCEPTION_MESSAGE
	}

}
