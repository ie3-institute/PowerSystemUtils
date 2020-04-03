/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util

import spock.lang.Shared
import spock.lang.Specification

import java.time.ZoneId
import java.time.ZonedDateTime


class TimeUtilsTest extends Specification {

	@Shared
	TimeUtil timeUtil = new TimeUtil(ZoneId.of("UTC"), Locale.GERMANY, "dd/MM/yyyy HH:mm:ss")


	def "A TimeUtil should provide a default constant with expected configuration"() {
		expect:
		verifyAll(TimeUtil.withDefaults) {
			zoneId == ZoneId.of("UTC")
			locale == Locale.GERMANY
			dtfPattern == "yyyy-MM-dd HH:mm:ss"
		}
	}

	def "A TimeUtil should convert given instances to a String correctly"() {
		given:
		ZonedDateTime testDate = ZonedDateTime.of(1990, 1, 1, 0, 15, 0, 0, ZoneId.of("UTC"))


		expect:
		timeUtil.toString(testDate) == "01/01/1990 00:15:00"
		timeUtil.toString(testDate.toInstant()) == "01/01/1990 00:15:00"
	}
}




