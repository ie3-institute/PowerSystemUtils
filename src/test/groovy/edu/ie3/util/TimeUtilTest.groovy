/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util

import spock.lang.Specification

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


class TimeUtilTest extends Specification {

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
		TimeUtil timeUtil = new TimeUtil(ZoneId.of("UTC"), Locale.GERMANY, "dd/MM/yyyy HH:mm:ss")
		ZonedDateTime testDate = ZonedDateTime.of(1990, 1, 1, 0, 15, 0, 0, ZoneId.of("UTC"))


		expect:
		timeUtil.toString(testDate) == "01/01/1990 00:15:00"
		timeUtil.toString(testDate.toInstant()) == "01/01/1990 00:15:00"
	}

	def "A TimeUtil should determine the correct quarter hour of the day within a quarter hour"() {
		expect:
		def time = TimeUtil.withDefaults.toZonedDateTime(timeStamp)
		TimeUtil.withDefaults.getQuarterHourOfDay(time) == expected

		where:
		timeStamp             || expected
		"2020-04-22 00:00:00" || 0
		"2020-04-22 00:01:00" || 0
		"2020-04-22 00:02:00" || 0
		"2020-04-22 00:03:00" || 0
		"2020-04-22 00:04:00" || 0
		"2020-04-22 00:05:00" || 0
		"2020-04-22 00:06:00" || 0
		"2020-04-22 00:07:00" || 0
		"2020-04-22 00:08:00" || 0
		"2020-04-22 00:09:00" || 0
		"2020-04-22 00:10:00" || 0
		"2020-04-22 00:11:00" || 0
		"2020-04-22 00:12:00" || 0
		"2020-04-22 00:13:00" || 0
		"2020-04-22 00:14:00" || 0
		"2020-04-22 00:14:59" || 0
		"2020-04-22 00:15:00" || 1
	}

	def "A TimeUtil should determine the correct quarter hour for each quarter hour of the day"() {
		expect:
		def time = TimeUtil.withDefaults.toZonedDateTime(timeStamp)
		TimeUtil.withDefaults.getQuarterHourOfDay(time) == expected

		where:
		timeStamp             || expected
		"2020-04-22 00:00:00" || 0
		"2020-04-22 00:15:00" || 1
		"2020-04-22 00:30:00" || 2
		"2020-04-22 00:45:00" || 3
		"2020-04-22 01:00:00" || 4
		"2020-04-22 01:15:00" || 5
		"2020-04-22 01:30:00" || 6
		"2020-04-22 01:45:00" || 7
		"2020-04-22 02:00:00" || 8
		"2020-04-22 02:15:00" || 9
		"2020-04-22 02:30:00" || 10
		"2020-04-22 02:45:00" || 11
		"2020-04-22 03:00:00" || 12
		"2020-04-22 03:15:00" || 13
		"2020-04-22 03:30:00" || 14
		"2020-04-22 03:45:00" || 15
		"2020-04-22 04:00:00" || 16
		"2020-04-22 04:15:00" || 17
		"2020-04-22 04:30:00" || 18
		"2020-04-22 04:45:00" || 19
		"2020-04-22 05:00:00" || 20
		"2020-04-22 05:15:00" || 21
		"2020-04-22 05:30:00" || 22
		"2020-04-22 05:45:00" || 23
		"2020-04-22 06:00:00" || 24
		"2020-04-22 06:15:00" || 25
		"2020-04-22 06:30:00" || 26
		"2020-04-22 06:45:00" || 27
		"2020-04-22 07:00:00" || 28
		"2020-04-22 07:15:00" || 29
		"2020-04-22 07:30:00" || 30
		"2020-04-22 07:45:00" || 31
		"2020-04-22 08:00:00" || 32
		"2020-04-22 08:15:00" || 33
		"2020-04-22 08:30:00" || 34
		"2020-04-22 08:45:00" || 35
		"2020-04-22 09:00:00" || 36
		"2020-04-22 09:15:00" || 37
		"2020-04-22 09:30:00" || 38
		"2020-04-22 09:45:00" || 39
		"2020-04-22 10:00:00" || 40
		"2020-04-22 10:15:00" || 41
		"2020-04-22 10:30:00" || 42
		"2020-04-22 10:45:00" || 43
		"2020-04-22 11:00:00" || 44
		"2020-04-22 11:15:00" || 45
		"2020-04-22 11:30:00" || 46
		"2020-04-22 11:45:00" || 47
		"2020-04-22 12:00:00" || 48
		"2020-04-22 12:15:00" || 49
		"2020-04-22 12:30:00" || 50
		"2020-04-22 12:45:00" || 51
		"2020-04-22 13:00:00" || 52
		"2020-04-22 13:15:00" || 53
		"2020-04-22 13:30:00" || 54
		"2020-04-22 13:45:00" || 55
		"2020-04-22 14:00:00" || 56
		"2020-04-22 14:15:00" || 57
		"2020-04-22 14:30:00" || 58
		"2020-04-22 14:45:00" || 59
		"2020-04-22 15:00:00" || 60
		"2020-04-22 15:15:00" || 61
		"2020-04-22 15:30:00" || 62
		"2020-04-22 15:45:00" || 63
		"2020-04-22 16:00:00" || 64
		"2020-04-22 16:15:00" || 65
		"2020-04-22 16:30:00" || 66
		"2020-04-22 16:45:00" || 67
		"2020-04-22 17:00:00" || 68
		"2020-04-22 17:15:00" || 69
		"2020-04-22 17:30:00" || 70
		"2020-04-22 17:45:00" || 71
		"2020-04-22 18:00:00" || 72
		"2020-04-22 18:15:00" || 73
		"2020-04-22 18:30:00" || 74
		"2020-04-22 18:45:00" || 75
		"2020-04-22 19:00:00" || 76
		"2020-04-22 19:15:00" || 77
		"2020-04-22 19:30:00" || 78
		"2020-04-22 19:45:00" || 79
		"2020-04-22 20:00:00" || 80
		"2020-04-22 20:15:00" || 81
		"2020-04-22 20:30:00" || 82
		"2020-04-22 20:45:00" || 83
		"2020-04-22 21:00:00" || 84
		"2020-04-22 21:15:00" || 85
		"2020-04-22 21:30:00" || 86
		"2020-04-22 21:45:00" || 87
		"2020-04-22 22:00:00" || 88
		"2020-04-22 22:15:00" || 89
		"2020-04-22 22:30:00" || 90
		"2020-04-22 22:45:00" || 91
		"2020-04-22 23:00:00" || 92
		"2020-04-22 23:15:00" || 93
		"2020-04-22 23:30:00" || 94
		"2020-04-22 23:45:00" || 95
	}

	def "A TimeUtil is able to round to the next full chrono unit"() {
		expect:
		def time = TimeUtil.withDefaults.toZonedDateTime(timeStamp)
		def expectedTime = TimeUtil.withDefaults.toZonedDateTime(expected)
		TimeUtil.toNextFull(time, chronoUnit) == expectedTime

		where:
		timeStamp             || chronoUnit         || expected
		"2020-04-21 23:59:00" || ChronoUnit.HOURS   || "2020-04-22 00:00:00"
		"2020-04-22 00:00:00" || ChronoUnit.HOURS   || "2020-04-22 00:00:00"
		"2020-04-22 00:01:00" || ChronoUnit.HOURS   || "2020-04-22 01:00:00"
		"2020-04-22 00:59:50" || ChronoUnit.HOURS   || "2020-04-22 01:00:00"
		"2020-04-22 00:59:50" || ChronoUnit.HOURS   || "2020-04-22 01:00:00"
		"2020-04-22 01:00:00" || ChronoUnit.DAYS    || "2020-04-23 00:00:00"
		"2020-04-21 23:59:01" || ChronoUnit.MINUTES || "2020-04-22 00:00:00"
	}
}


