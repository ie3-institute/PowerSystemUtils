package edu.ie3.util.interval

import spock.lang.Shared
import spock.lang.Specification

class OpenIntervalTest extends Specification {
    @Shared
    OpenInterval<Integer> dut

    def setupSpec() {
        dut = new OpenInterval<>(1, 3)
    }

    def "Valid constructor test"() {
        when:
        OpenInterval dut = new OpenInterval<>(1, 3)

        then:
        dut.getLower() == 1
        dut.getUpper() == 3
    }

    def "Lower bound null test"() {
        when:
        new OpenInterval<>(null, 3)

        then:
        NullPointerException exception = thrown(NullPointerException.class)
        exception.message == "Bound must not be null"
    }

    def "Upper bound null test"() {
        when:
        new OpenInterval<>(1, null)

        then:
        NullPointerException exception = thrown(NullPointerException.class)
        exception.message == "Bound must not be null"
    }

    def "Twisted bounds test"() {
        when:
        new OpenInterval<>(3, 1)

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
        1 | false
        2 | true
        3 | false
        4 | false
    }
}
