/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util

import spock.lang.Specification

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class TimeUtilTest extends Specification {


	def "A TimeUtil should convert given instances to a String correctly"() {
		given:
		TimeUtil timeUtil = new TimeUtil(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
		ZonedDateTime testDate = ZonedDateTime.of(1990, 1, 1, 0, 15, 0, 0, ZoneId.of("UTC"))

		expect:
		timeUtil.toString(testDate) == "01/01/1990 00:15:00"
		timeUtil.toString(testDate.toInstant()) == "01/01/1990 00:15:00"
	}

	def "A TimeUtil should convert ZonedDateTime objects to local date time strings correctly"() {
		given:
		def zonedDateTime = ZonedDateTime.parse("2020-04-22T00:00:00+01:00")

		expect:
		TimeUtil.withDefaults.toLocalDateTimeString(zonedDateTime) == "2020-04-22 00:00:00"
	}

	def "A TimeUtil should parse ZonedDateTime objects with time zone correctly"() {
		when:
		def zonedDateTime = TimeUtil.withDefaults.toZonedDateTime("2020-04-22T00:00:00+01:00")

		then:
		DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime) == "2020-04-22T00:00:00+01:00"
	}

	def "A TimeUtil should determine the correct quarter hour of the day within a quarter hour"() {
		expect:
		def time = TimeUtil.withDefaults.toZonedDateTime(timeStamp)
		TimeUtil.withDefaults.getQuarterHourOfDay(time) == expected

		where:
		timeStamp             || expected
		"2020-04-22T00:00:00Z" || 0
		"2020-04-22T00:01:00Z" || 0
		"2020-04-22T00:02:00Z" || 0
		"2020-04-22T00:03:00Z" || 0
		"2020-04-22T00:04:00Z" || 0
		"2020-04-22T00:05:00Z" || 0
		"2020-04-22T00:06:00Z" || 0
		"2020-04-22T00:07:00Z" || 0
		"2020-04-22T00:08:00Z" || 0
		"2020-04-22T00:09:00Z" || 0
		"2020-04-22T00:10:00Z" || 0
		"2020-04-22T00:11:00Z" || 0
		"2020-04-22T00:12:00Z" || 0
		"2020-04-22T00:13:00Z" || 0
		"2020-04-22T00:14:00Z" || 0
		"2020-04-22T00:14:59Z" || 0
		"2020-04-22T00:15:00Z" || 1
	}

	def "A TimeUtil should determine the correct quarter hour for each quarter hour of the day"() {
		expect:
		def time = TimeUtil.withDefaults.toZonedDateTime(timeStamp)
		TimeUtil.withDefaults.getQuarterHourOfDay(time) == expected

		where:
		timeStamp             || expected
		"2020-04-22T00:00:00Z" || 0
		"2020-04-22T00:15:00Z" || 1
		"2020-04-22T00:30:00Z" || 2
		"2020-04-22T00:45:00Z" || 3
		"2020-04-22T01:00:00Z" || 4
		"2020-04-22T01:15:00Z" || 5
		"2020-04-22T01:30:00Z" || 6
		"2020-04-22T01:45:00Z" || 7
		"2020-04-22T02:00:00Z" || 8
		"2020-04-22T02:15:00Z" || 9
		"2020-04-22T02:30:00Z" || 10
		"2020-04-22T02:45:00Z" || 11
		"2020-04-22T03:00:00Z" || 12
		"2020-04-22T03:15:00Z" || 13
		"2020-04-22T03:30:00Z" || 14
		"2020-04-22T03:45:00Z" || 15
		"2020-04-22T04:00:00Z" || 16
		"2020-04-22T04:15:00Z" || 17
		"2020-04-22T04:30:00Z" || 18
		"2020-04-22T04:45:00Z" || 19
		"2020-04-22T05:00:00Z" || 20
		"2020-04-22T05:15:00Z" || 21
		"2020-04-22T05:30:00Z" || 22
		"2020-04-22T05:45:00Z" || 23
		"2020-04-22T06:00:00Z" || 24
		"2020-04-22T06:15:00Z" || 25
		"2020-04-22T06:30:00Z" || 26
		"2020-04-22T06:45:00Z" || 27
		"2020-04-22T07:00:00Z" || 28
		"2020-04-22T07:15:00Z" || 29
		"2020-04-22T07:30:00Z" || 30
		"2020-04-22T07:45:00Z" || 31
		"2020-04-22T08:00:00Z" || 32
		"2020-04-22T08:15:00Z" || 33
		"2020-04-22T08:30:00Z" || 34
		"2020-04-22T08:45:00Z" || 35
		"2020-04-22T09:00:00Z" || 36
		"2020-04-22T09:15:00Z" || 37
		"2020-04-22T09:30:00Z" || 38
		"2020-04-22T09:45:00Z" || 39
		"2020-04-22T10:00:00Z" || 40
		"2020-04-22T10:15:00Z" || 41
		"2020-04-22T10:30:00Z" || 42
		"2020-04-22T10:45:00Z" || 43
		"2020-04-22T11:00:00Z" || 44
		"2020-04-22T11:15:00Z" || 45
		"2020-04-22T11:30:00Z" || 46
		"2020-04-22T11:45:00Z" || 47
		"2020-04-22T12:00:00Z" || 48
		"2020-04-22T12:15:00Z" || 49
		"2020-04-22T12:30:00Z" || 50
		"2020-04-22T12:45:00Z" || 51
		"2020-04-22T13:00:00Z" || 52
		"2020-04-22T13:15:00Z" || 53
		"2020-04-22T13:30:00Z" || 54
		"2020-04-22T13:45:00Z" || 55
		"2020-04-22T14:00:00Z" || 56
		"2020-04-22T14:15:00Z" || 57
		"2020-04-22T14:30:00Z" || 58
		"2020-04-22T14:45:00Z" || 59
		"2020-04-22T15:00:00Z" || 60
		"2020-04-22T15:15:00Z" || 61
		"2020-04-22T15:30:00Z" || 62
		"2020-04-22T15:45:00Z" || 63
		"2020-04-22T16:00:00Z" || 64
		"2020-04-22T16:15:00Z" || 65
		"2020-04-22T16:30:00Z" || 66
		"2020-04-22T16:45:00Z" || 67
		"2020-04-22T17:00:00Z" || 68
		"2020-04-22T17:15:00Z" || 69
		"2020-04-22T17:30:00Z" || 70
		"2020-04-22T17:45:00Z" || 71
		"2020-04-22T18:00:00Z" || 72
		"2020-04-22T18:15:00Z" || 73
		"2020-04-22T18:30:00Z" || 74
		"2020-04-22T18:45:00Z" || 75
		"2020-04-22T19:00:00Z" || 76
		"2020-04-22T19:15:00Z" || 77
		"2020-04-22T19:30:00Z" || 78
		"2020-04-22T19:45:00Z" || 79
		"2020-04-22T20:00:00Z" || 80
		"2020-04-22T20:15:00Z" || 81
		"2020-04-22T20:30:00Z" || 82
		"2020-04-22T20:45:00Z" || 83
		"2020-04-22T21:00:00Z" || 84
		"2020-04-22T21:15:00Z" || 85
		"2020-04-22T21:30:00Z" || 86
		"2020-04-22T21:45:00Z" || 87
		"2020-04-22T22:00:00Z" || 88
		"2020-04-22T22:15:00Z" || 89
		"2020-04-22T22:30:00Z" || 90
		"2020-04-22T22:45:00Z" || 91
		"2020-04-22T23:00:00Z" || 92
		"2020-04-22T23:15:00Z" || 93
		"2020-04-22T23:30:00Z" || 94
		"2020-04-22T23:45:00Z" || 95
	}

	def "A TimeUtil is able to round to the next full chrono unit"() {
		expect:
		def time = TimeUtil.withDefaults.toZonedDateTime(timeStamp)
		def expectedTime = TimeUtil.withDefaults.toZonedDateTime(expected)
		TimeUtil.toNextFull(time, chronoUnit) == expectedTime

		where:
		timeStamp             || chronoUnit         || expected
		"2020-04-21T23:59:00Z" || ChronoUnit.HOURS   || "2020-04-22T00:00:00Z"
		"2020-04-22T00:00:00Z" || ChronoUnit.HOURS   || "2020-04-22T00:00:00Z"
		"2020-04-22T00:01:00Z" || ChronoUnit.HOURS   || "2020-04-22T01:00:00Z"
		"2020-04-22T00:59:50Z" || ChronoUnit.HOURS   || "2020-04-22T01:00:00Z"
		"2020-04-22T00:59:50Z" || ChronoUnit.HOURS   || "2020-04-22T01:00:00Z"
		"2020-04-22T01:00:00Z" || ChronoUnit.DAYS    || "2020-04-23T00:00:00Z"
		"2020-04-21T23:59:01Z" || ChronoUnit.MINUTES || "2020-04-22T00:00:00Z"
	}
}


