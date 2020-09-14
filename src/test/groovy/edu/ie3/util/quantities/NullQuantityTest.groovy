/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import spock.lang.Specification
import tech.units.indriya.quantity.Quantities

class NullQuantityTest extends Specification {

	def "A NullQuantity can be initialized for a specific Unit and has 'null' as value"() {
		when:
		nullQuantity

		then:
		nullQuantity != null
		nullQuantity.getUnit() == expectedUnit
		nullQuantity.getValue() == null

		where:
		nullQuantity                            || expectedUnit
		NullQuantity.of(PowerSystemUnits.METRE) || PowerSystemUnits.METRE
		NullQuantity.of(PowerSystemUnits.WATT)  || PowerSystemUnits.WATT
	}

	def "NullQuantity is never equivalent or equal to a regular Quantity "() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)

		then:
		nullMetreQuantity != filledMetreQuantity
		filledMetreQuantity != nullMetreQuantity
		!nullMetreQuantity.isEquivalentTo(filledMetreQuantity)
	}

	def "NullQuantity is always equivalent to another NullQuantity "() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def nullWattQuantity = NullQuantity.of(PowerSystemUnits.WATT)

		then:
		nullMetreQuantity.isEquivalentTo(nullWattQuantity)
		//no, this cannot be replaced with an operator, because then it routes over AbstractQuantity and uses that equals method first
		nullMetreQuantity.equals(nullWattQuantity)
	}

	def "NullQuantity.quantityIsNull(Quantity) only returns true if the provided Quantity is null or NullQuantity"() {
		when:
		nullQuantity

		then:
		NullQuantity.quantityIsNull(nullQuantity) == expectedResult

		where:
		nullQuantity                                         || expectedResult
		null                                                 || true
		NullQuantity.of(PowerSystemUnits.METRE)              || true
		Quantities.getQuantity(0, PowerSystemUnits.METRE)    || false
		Quantities.getQuantity(17.1, PowerSystemUnits.METRE) || false
	}

	//sadly, we have no influence here, so we can't use a custom exception message
	def "If a regular Quantity is compared to a NullQuantity, a NullPointer is thrown"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		filledMetreQuantity.isEquivalentTo(nullMetreQuantity)

		then:
		thrown NullPointerException
	}

	def "The add operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.add(filledMetreQuantity)

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The subtract operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.subtract(filledMetreQuantity)

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The divide by Quantity operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.divide(filledMetreQuantity)

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The divide by Number operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def divisor = 17.1
		nullMetreQuantity.divide(divisor)

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The multiply by Quantity operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def filledMetreQuantity = Quantities.getQuantity(17.1, PowerSystemUnits.METRE)
		nullMetreQuantity.multiply(filledMetreQuantity)

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The multiply by Number operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		def multiplier = 17.1
		nullMetreQuantity.multiply(multiplier)

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The inverse operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		nullMetreQuantity.inverse()

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

	def "The negate operation on a NullQuantity throws a NullPointer exception with a custom message"() {
		when:
		def nullMetreQuantity = NullQuantity.of(PowerSystemUnits.METRE)
		nullMetreQuantity.negate()

		then:
		def exception = thrown(NullPointerException)
		exception.message == NullQuantity.EXCEPTION_MESSAGE
	}

}
