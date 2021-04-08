/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.naming

import spock.lang.Specification

import static edu.ie3.util.naming.NamingConvention.*

class NamingTest extends Specification {
	def "Camel casing a single component works properly"() {
		given:
		def components = new LinkedList(["hello"])

		when:
		def actual = Naming.camelCasing(components)

		then:
		actual.size() == 1
		actual.first == "hello"
	}

	def "Camel casing two word component works properly"() {
		given:
		def components = new LinkedList(["hello", "world"])

		when:
		def actual = Naming.camelCasing(components)

		then:
		actual.size() == 2
		String.join("", actual) == "helloWorld"
	}

	def "When camel casing, a digit blocks capitalization"() {
		given:
		def components = new LinkedList([
			"welcome",
			"2",
			"this",
			"world"
		])

		when:
		def actual = Naming.camelCasing(components)

		then:
		actual.size() == 4
		String.join("", actual) == "welcome2thisWorld"
	}

	def "When camel casing, a single character component blocks capitalization"() {
		given:
		def components = new LinkedList([
			"what",
			"a",
			"beautiful",
			"world"
		])

		when:
		def actual = Naming.camelCasing(components)

		then:
		actual.size() == 4
		String.join("", actual) == "whatAbeautifulWorld"
	}

	def "Building a naming should neglect components with delimiter characters"() {
		when:
		def actualNaming = Naming.from("hello", "-", "world", "whatever|")

		then:
		actualNaming.flatCase == "helloworld"
	}

	def "Building a naming should accept screaming"() {
		when:
		def actualNaming = Naming.from("HELLO", "WORLD")

		then:
		actualNaming.flatCase == "helloworld"
	}

	def "Naming builds correct flat cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.flatCase == expected
		actualNaming.as(Flat) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "hello"
		["hello", "world"] as String[] || "helloworld"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "welcome2thisworld"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "whatabeautifulworld"
	}

	def "Naming builds correct upper flat cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.upperFlatCase == expected
		actualNaming.as(UpperFlat) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "HELLO"
		["hello", "world"] as String[] || "HELLOWORLD"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "WELCOME2THISWORLD"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "WHATABEAUTIFULWORLD"
	}

	def "Naming builds correct camel cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.camelCase == expected
		actualNaming.as(Camel) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "hello"
		["hello", "world"] as String[] || "helloWorld"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "welcome2thisWorld"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "whatAbeautifulWorld"
	}

	def "Naming builds correct pascal cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.pascalCase == expected
		actualNaming.as(Pascal) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "Hello"
		["hello", "world"] as String[] || "HelloWorld"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "Welcome2ThisWorld"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "WhatABeautifulWorld"
	}

	def "Naming builds correct snake cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.snakeCase == expected
		actualNaming.as(Snake) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "hello"
		["hello", "world"] as String[] || "hello_world"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "welcome_2_this_world"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "what_a_beautiful_world"
	}

	def "Naming builds correct screaming snake cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.screamingSnakeCase == expected
		actualNaming.as(ScreamingSnake) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "HELLO"
		["hello", "world"] as String[] || "HELLO_WORLD"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "WELCOME_2_THIS_WORLD"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "WHAT_A_BEAUTIFUL_WORLD"
	}

	def "Naming builds correct camel snake cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.camelSnakeCase == expected
		actualNaming.as(CamelSnake) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "hello"
		["hello", "world"] as String[] || "hello_World"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "welcome_2_this_World"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "what_A_beautiful_World"
	}

	def "Naming builds correct pascal snake cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.pascalSnakeCase == expected
		actualNaming.as(PascalSnake) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "Hello"
		["hello", "world"] as String[] || "Hello_World"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "Welcome_2_This_World"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "What_A_Beautiful_World"
	}

	def "Naming builds correct kebab cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.kebabCase == expected
		actualNaming.as(Kebab) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "hello"
		["hello", "world"] as String[] || "hello-world"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "welcome-2-this-world"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "what-a-beautiful-world"
	}

	def "Naming builds correct doner cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.donerCase == expected
		actualNaming.as(Doner) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "hello"
		["hello", "world"] as String[] || "hello|world"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "welcome|2|this|world"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "what|a|beautiful|world"
	}

	def "Naming builds correct screaming kebab cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.screamingKebabCase == expected
		actualNaming.as(ScreamingKebab) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "HELLO"
		["hello", "world"] as String[] || "HELLO-WORLD"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "WELCOME-2-THIS-WORLD"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "WHAT-A-BEAUTIFUL-WORLD"
	}

	def "Naming builds correct train cases"() {
		when:
		def actualNaming = Naming.from(input)

		then:
		actualNaming.trainCase == expected
		actualNaming.as(Train) == expected

		where:
		input                          || expected
		["hello"] as String[]          || "Hello"
		["hello", "world"] as String[] || "Hello-World"
		[
			"welcome",
			"2",
			"this",
			"world"] as String[]   || "Welcome-2-This-World"
		[
			"what",
			"a",
			"beautiful",
			"world"] as String[]   || "What-A-Beautiful-World"
	}
}
