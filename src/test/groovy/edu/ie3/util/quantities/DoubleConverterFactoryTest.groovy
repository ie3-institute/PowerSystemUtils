package edu.ie3.util.quantities

import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units

import spock.lang.Specification

import javax.measure.MetricPrefix

class DoubleConverterFactoryTest extends Specification  {

    def "creates a unit with a prefix correctly"()  {
        when:
        def prefixedUnit = DoubleConverterFactory.withPrefix(baseUnit, prefix)

        then:
        prefixedUnit.symbol == expectedSymbol

        def testValue = 12345.6789d
        def factor = Math.pow(prefix.value, prefix.exponent)
        def testQuantity = Quantities.getQuantity(testValue, prefixedUnit)

        def inBaseUnit = testQuantity.to(baseUnit)
        inBaseUnit == Quantities.getQuantity(testValue * factor, baseUnit)
        inBaseUnit.to(prefixedUnit).value == Quantities.getQuantity(testValue, baseUnit).value

        where:
        baseUnit             | prefix             || expectedSymbol
        Units.WATT           | MetricPrefix.KILO  || "kW"
        Units.WATT           | MetricPrefix.MILLI || "mW"
        Units.SECOND         | MetricPrefix.NANO  || "ns"
        Units.AMPERE         | MetricPrefix.TERA  || "TA"
        PowerSystemUnits.VAR | MetricPrefix.MEGA  || "Mvar"
    }

    def "creates a unit with a prefix and given symbol correctly"()  {
        when:
        def prefixedUnit = DoubleConverterFactory.withPrefix(baseUnit, prefix, symbol)

        then:
        prefixedUnit.symbol == symbol

        def testValue = 12345.6789d
        def factor = Math.pow(prefix.value, prefix.exponent)
        def testQuantity = Quantities.getQuantity(testValue, prefixedUnit)

        def inBaseUnit = testQuantity.to(baseUnit)
        inBaseUnit == Quantities.getQuantity(testValue * factor, baseUnit)
        inBaseUnit.to(prefixedUnit).value == Quantities.getQuantity(testValue, baseUnit).value

        where:
        baseUnit             | prefix             || symbol
        Units.WATT           | MetricPrefix.KILO  || "kW"
        Units.WATT           | MetricPrefix.MILLI || "mW"
        Units.SECOND         | MetricPrefix.NANO  || "ns"
        Units.AMPERE         | MetricPrefix.TERA  || "TA"
        PowerSystemUnits.VAR | MetricPrefix.MEGA  || "Mvar"
    }
}