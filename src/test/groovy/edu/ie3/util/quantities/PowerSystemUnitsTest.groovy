/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.quantities

import spock.lang.Shared
import spock.lang.Specification
import tech.units.indriya.quantity.Quantities

import static edu.ie3.util.quantities.PowerSystemUnits.*
import static tech.units.indriya.unit.Units.JOULE
import static tech.units.indriya.unit.Units.RADIAN

class PowerSystemUnitsTest extends Specification {
	@Shared
	double testingTolerance = 1E-9

	def "Transformed units are transferred correctly to their base units"() {
		when:
		def actualQuantity = quantity.to(baseUnit)

		then:
		QuantityUtil.equals(expectedQuantity, actualQuantity, testingTolerance)

		where:
		quantity                                               | baseUnit            || expectedQuantity
		Quantities.getQuantity(1d, EURO_PER_KILOWATTHOUR)      | EURO_PER_WATTHOUR   || Quantities.getQuantity(1E-3, EURO_PER_WATTHOUR)
		Quantities.getQuantity(1d, EURO_PER_MEGAWATTHOUR)      | EURO_PER_WATTHOUR   || Quantities.getQuantity(1E-6, EURO_PER_WATTHOUR)
		Quantities.getQuantity(1d, DEGREE_GEOM)                | RADIAN              || Quantities.getQuantity(0.017453292, RADIAN)
		Quantities.getQuantity(1d, WATTHOUR)                   | JOULE               || Quantities.getQuantity(3600d, JOULE)
		Quantities.getQuantity(1d, VARHOUR)                    | JOULE               || Quantities.getQuantity(3600d, JOULE)
		Quantities.getQuantity(1d, KILOWATTHOUR_PER_KILOMETRE) | WATTHOUR_PER_METRE  || Quantities.getQuantity(1d, WATTHOUR_PER_METRE)
		Quantities.getQuantity(1d, PU_PER_HOUR)                | PERCENT_PER_HOUR    || Quantities.getQuantity(100d, PERCENT_PER_HOUR)
		Quantities.getQuantity(1d, FARAD_PER_KILOMETRE)        | FARAD_PER_METRE     || Quantities.getQuantity(1E-3, FARAD_PER_METRE)
		Quantities.getQuantity(1d, MICROFARAD_PER_KILOMETRE)   | FARAD_PER_KILOMETRE || Quantities.getQuantity(1E-6, FARAD_PER_KILOMETRE)
		Quantities.getQuantity(1d, MICROFARAD_PER_KILOMETRE)   | FARAD_PER_METRE     || Quantities.getQuantity(1E-9, FARAD_PER_METRE)
	}

	def "Angle quantities are transferred from degrees to radians correctly"() {
		given:
		def degreeQuantity = Quantities.getQuantity(degree, DEGREE_GEOM)

		when:
		def actualValue = degreeQuantity.to(RADIAN).value.doubleValue()

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
		def radianQuantity = Quantities.getQuantity(radians, RADIAN)

		when:
		def actualValue = radianQuantity.to(DEGREE_GEOM).value.doubleValue()

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
