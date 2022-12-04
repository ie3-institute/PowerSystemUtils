package edu.ie3.util.quantities

import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units

import spock.lang.Specification

import javax.measure.MetricPrefix

class MetricPrefixDoubleTest extends Specification  {

    def "combines a unit with a prefix correctly"()  {
        when:
        def prefixedUnit = MetricPrefixDouble.prefix(prefix, baseUnit)

        then:
        def testValue = 12345.6789d
        def factor = Math.pow(prefix.value, prefix.exponent)
        def testQuantity = Quantities.getQuantity(testValue, prefixedUnit)

        def inBaseUnit = testQuantity.to(baseUnit)
        inBaseUnit == Quantities.getQuantity(testValue * factor, baseUnit)
        inBaseUnit.to(prefixedUnit).value == Quantities.getQuantity(testValue, baseUnit).value

        where:
        baseUnit     | prefix
        Units.WATT   | MetricPrefix.KILO
        Units.WATT   | MetricPrefix.MILLI
        Units.SECOND | MetricPrefix.NANO
        Units.AMPERE | MetricPrefix.TERA
    }
}