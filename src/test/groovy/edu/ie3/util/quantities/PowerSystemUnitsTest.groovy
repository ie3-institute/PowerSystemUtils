/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import spock.lang.Specification
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units

class PowerSystemUnitsTest extends Specification {
	def "Angle quantities are transferred from degrees to radians correctly"() {
		given:
		def degreeQuantity = Quantities.getQuantity(degree, PowerSystemUnits.DEGREE_GEOM)

		when:
		def actualValue = degreeQuantity.to(Units.RADIAN).value.doubleValue()

		then:
		Math.abs(actualValue - radians) < 1E-12

		where:
		degree || radians
		0.0    || 0.0
		90.0   || Math.PI / 2
		180.0  || Math.PI
		270.0  || Math.PI * 3 / 2
	}

	def "Angle quantities are transferred from radians to radians degree"() {
		given:
		def radianQuantity = Quantities.getQuantity(radians, Units.RADIAN)

		when:
		def actualValue = radianQuantity.to(PowerSystemUnits.DEGREE_GEOM).value.doubleValue()

		then:
		Math.abs(actualValue - degree) < 1E-12

		where:
		radians         || degree
		0.0             || 0.0
		Math.PI / 2     || 90.0
		Math.PI         || 180.0
		Math.PI * 3 / 2 || 270.0
	}
}
