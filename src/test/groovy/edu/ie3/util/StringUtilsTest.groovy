/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util

import spock.lang.Specification

import java.util.stream.Collectors

class StringUtilsTest extends Specification {

	def "The StringUtils quote a single String correctly"() {
		when:
		def actual = StringUtils.quote(input)

		then:
		actual == expected

		where:
		input                || expected
		"test"               || "\"test\""
		"\"test"             || "\"\"test\""
		"test\""             || "\"test\"\""
		"\"test\""           || "\"test\""
		"\"This\" is a test" || "\"\"This\" is a test\""
		"This is \"a\" test" || "\"This is \"a\" test\""
		"This is a \"test\"" || "\"This is a \"test\"\""
	}

	def "The StringUtils are able to quote each element of an array of Strings"() {
		given:
		def input = [
			"inputModel",
			"iAMag",
			"timestamp",
			"p",
			"nodeC",
			"tapPos",
			"noOfParallelDevices",
			"kWd",
			"mySa",
			"sRated",
			"xScA",
			"sRatedB"] as String[]
		def expected = [
			"\"inputModel\"",
			"\"iAMag\"",
			"\"timestamp\"",
			"\"p\"",
			"\"nodeC\"",
			"\"tapPos\"",
			"\"noOfParallelDevices\"",
			"\"kWd\"",
			"\"mySa\"",
			"\"sRated\"",
			"\"xScA\"",
			"\"sRatedB\""] as String[]

		when:
		def actual = StringUtils.quote(input)

		then:
		actual == expected
	}

	def "The StringUtils convert a given camel case correctly to snake case"() {
		when:
		def actual = StringUtils.camelCaseToSnakeCase(input)

		then:
		actual == expected

		where:
		input       || expected
		"helloDude" || "hello_dude"
		"2Be"       || "2_be"
		"orBe2"     || "or_be_2"
		//		"came2win"	|| "came_2_win" // currently not covered by the method
		//		"2be"		|| "2_be"		// currently not covered by the method
		//		"orBE2"		|| "or_be_2" 	// currently not covered by the method
	}

	def "The StringUtils convert a given snake case correctly to lower camel case"() {
		when:
		def actual = StringUtils.snakeCaseToCamelCase(input)

		then:
		actual == expected

		where:
		input        || expected
		"hello_dude" || "helloDude"
		"2_be"       || "2Be"
		"or_be_2"    || "orBe2"
		"came_2_win" || "came2Win"
	}

	def "The StringUtils convert a given snake case correctly to upper camel case (=pascal case)"() {
		when:
		def actual = StringUtils.snakeCaseToPascalCase(input)

		then:
		actual == expected

		where:
		input        || expected
		"hello_dude" || "HelloDude"
		"2_be"       || "2Be"
		"or_be_2"    || "OrBe2"
		"came_2_win" || "Came2Win"
	}

	def "The StringUtils convert a given Array of camel case Strings correctly to snake case"() {
		given:
		def input = [
			"inputModel",
			"iAMag",
			"timestamp",
			"p",
			"nodeC",
			"tapPos",
			"noOfParallelDevices",
			"kWd",
			"mySa",
			"sRated",
			"xScA",
			"sRatedB"] as String[]
		def expected = [
			"input_model",
			"i_a_mag",
			"timestamp",
			"p",
			"node_c",
			"tap_pos",
			"no_of_parallel_devices",
			"k_wd",
			"my_sa",
			"s_rated",
			"x_sc_a",
			"s_rated_b"] as String[]

		when:
		def actual = StringUtils.camelCaseToSnakeCase(input)

		then:
		actual == expected
	}

	def "The StringUtils are capable of cleaning up strings correctly"() {
		when:
		def actual = StringUtils.cleanString(input)

		then:
		actual == expected

		where:
		input     || expected
		"ab123"   || "ab123"
		"ab.123"  || "ab_123"
		"ab-123"  || "ab_123"
		"ab_123"  || "ab_123"
		"ab/123"  || "ab_123"
		"ab\\123" || "ab_123"
		"ab!123"  || "ab_123"
		"ab\"123" || "ab_123"
		"ab§123"  || "ab_123"
		"ab\$123" || "ab_123"
		"ab&123"  || "ab_123"
		"ab{123"  || "ab_123"
		"ab[123"  || "ab_123"
		"ab}123"  || "ab_123"
		"ab]123"  || "ab_123"
		"ab(123"  || "ab_123"
		"ab)123"  || "ab_123"
		"ab=123"  || "ab_123"
		"ab?123"  || "ab_123"
		"abß123"  || "ab_123"
		"ab123."  || "ab123_"
		"ab123-"  || "ab123_"
		"ab123_"  || "ab123_"
		"ab123/"  || "ab123_"
		"ab123\\" || "ab123_"
		"ab123!"  || "ab123_"
		"ab123\"" || "ab123_"
		"ab123§"  || "ab123_"
		"ab123\$" || "ab123_"
		"ab123&"  || "ab123_"
		"ab123{"  || "ab123_"
		"ab123["  || "ab123_"
		"ab123}"  || "ab123_"
		"ab123]"  || "ab123_"
		"ab123("  || "ab123_"
		"ab123)"  || "ab123_"
		"ab123="  || "ab123_"
		"ab123?"  || "ab123_"
		"ab123ß"  || "ab123_"
		".ab123"  || "_ab123"
		"-ab123"  || "_ab123"
		"_ab123"  || "_ab123"
		"/ab123"  || "_ab123"
		"\\ab123" || "_ab123"
		"!ab123"  || "_ab123"
		"\"ab123" || "_ab123"
		"§ab123"  || "_ab123"
		"\$ab123" || "_ab123"
		"&ab123"  || "_ab123"
		"{ab123"  || "_ab123"
		"[ab123"  || "_ab123"
		"}ab123"  || "_ab123"
		"]ab123"  || "_ab123"
		"(ab123"  || "_ab123"
		")ab123"  || "_ab123"
		"=ab123"  || "_ab123"
		"?ab123"  || "_ab123"
		"ßab123"  || "_ab123"
	}

	def "The StringUtils converts a given Array of csv header elements to match the csv specification RFC 4180 "() {
		given:
		def input = [
			"4ca90220-74c2-4369-9afa-a18bf068840d",
			"{\"type\":\"Point\",\"coordinates\":[7.411111,51.492528],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4326\"}}}",
			"node_a",
			"2020-03-25T15:11:31Z[UTC] \n 2020-03-24T15:11:31Z[UTC]",
			"8f9682df-0744-4b58-a122-f0dc730f6510",
			"true",
			"1,0",
			"1.0",
			"Höchstspannung",
			"380.0",
			"olm:{(0.00,1.00)}",
			"cosPhiP:{(0.0,1.0),(0.9,1.0),(1.2,-0.3)}"
		]
		def expected = [
			"4ca90220-74c2-4369-9afa-a18bf068840d",
			"\"{\"\"type\"\":\"\"Point\"\",\"\"coordinates\"\":[7.411111,51.492528],\"\"crs\"\":{\"\"type\"\":\"\"name\"\",\"\"properties\"\":{\"\"name\"\":\"\"EPSG:4326\"\"}}}\"",
			"node_a",
			"\"2020-03-25T15:11:31Z[UTC] \n 2020-03-24T15:11:31Z[UTC]\"",
			"8f9682df-0744-4b58-a122-f0dc730f6510",
			"true",
			"\"1,0\"",
			"1.0",
			"Höchstspannung",
			"380.0",
			"\"olm:{(0.00,1.00)}\"",
			"\"cosPhiP:{(0.0,1.0),(0.9,1.0),(1.2,-0.3)}\""] as Set

		when:
		def actual = input.stream().map({ inputElement -> StringUtils.csvString(inputElement, ",") }).collect(Collectors.toSet()) as Set

		then:
		actual == expected
	}

	def "The StringUtils converts a given LinkedHashMap of csv data to match the csv specification RFC 4180 "() {
		given:
		def input = [
			"activePowerGradient": "25.0",
			"capex"              : "100,0",
			"cosphiRated"        : "0.95",
			"etaConv"            : "98.0",
			"id"                 : "test \n bmTypeInput",
			"opex"               : "50.0",
			"sRated"             : "25.0",
			"uu,id"              : "5ebd8f7e-dedb-4017-bb86-6373c4b68eb8",
			"geoPosition"        : "{\"type\":\"Point\",\"coordinates\":[7.411111,51.492528],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4326\"}}}",
			"olm\"characteristic": "olm:{(0.0,1.0)}",
			"cosPhiFixed"        : "cosPhiFixed:{(0.0,1.0)}"
		] as LinkedHashMap<String, String>

		def expected = [
			"activePowerGradient"      : "25.0",
			"capex"                    : "\"100,0\"",
			"cosphiRated"              : "0.95",
			"etaConv"                  : "98.0",
			"id"                       : "\"test \n bmTypeInput\"",
			"opex"                     : "50.0",
			"sRated"                   : "25.0",
			"\"uu,id\""                : "5ebd8f7e-dedb-4017-bb86-6373c4b68eb8",
			"geoPosition"              : "\"{\"\"type\"\":\"\"Point\"\",\"\"coordinates\"\":[7.411111,51.492528],\"\"crs\"\":{\"\"type\"\":\"\"name\"\",\"\"properties\"\":{\"\"name\"\":\"\"EPSG:4326\"\"}}}\"",
			"\"olm\"\"characteristic\"": "\"olm:{(0.0,1.0)}\"",
			"cosPhiFixed"              : "\"cosPhiFixed:{(0.0,1.0)}\""
		] as LinkedHashMap<String, String>

		when:
		def actualList = input.entrySet().stream().map({ mapEntry ->
			return new AbstractMap.SimpleEntry<String, String>(StringUtils.csvString(mapEntry.key, ","), StringUtils.csvString(mapEntry.value, ","))
		}) as Set

		def actual = actualList.collectEntries {
			[it.key, it.value]
		}

		then:
		actual == expected
	}

	def "The StringUtils converts a given String to match the csv specification RFC 4180 "() {
		expect:
		StringUtils.csvString(inputString, csvSep) == expect

		where:
		inputString                                                                         | csvSep || expect
		"activePowerGradient"                                                               | ","    || "activePowerGradient"
		"\"100,0\""                                                                         | ","    || "\"100,0\""
		"100,0"                                                                             | ","    || "\"100,0\""
		"100,0"                                                                             | ";"    || "\"100,0\""
		"100;0"                                                                             | ";"    || "\"100;0\""
		"\"100;0\""                                                                         | ";"    || "\"100;0\""
		"100;0"                                                                             | ","    || "100;0"
		"olm:{(0.00,1.00)}"                                                                 | ","    || "\"olm:{(0.00,1.00)}\""
		"olm:{(0.00,1.00)}"                                                                 | ";"    || "\"olm:{(0.00,1.00)}\""
		"{\"type\":\"Point\",\"coordinates\":[7.411111,51.492528]}"                         | ","    || "\"{\"\"type\"\":\"\"Point\"\",\"\"coordinates\"\":[7.411111,51.492528]}\""
		"{\"type\":\"Point\",\"coordinates\":[7.411111,51.492528]}"                         | ";"    || "\"{\"\"type\"\":\"\"Point\"\",\"\"coordinates\"\":[7.411111,51.492528]}\""
		"uu,id"                                                                             | ","    || "\"uu,id\""
		"uu,id"                                                                             | ";"    || "\"uu,id\""
	}
}
