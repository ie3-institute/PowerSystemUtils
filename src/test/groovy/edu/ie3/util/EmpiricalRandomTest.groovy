/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util
/*
 * Copyright (c) 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

import edu.ie3.util.EmpiricalRandom
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.eclipse.collections.impl.UnmodifiableMap
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author Kittl* @since 28.03.2019
 */
@Slf4j
class EmpiricalRandomTest extends Specification {
	@Shared
	HashMap<String, Double> dut = new HashMap<>()
	@Shared
	Map<String, Double> checkCdf = new HashMap<>()

	@TypeChecked
	def setup() {
		/* Build the map of empirical occurrences */
		dut.put("A", 1.0d)
		dut.put("B", 1.0d)
		dut.put("C", 0.5d)
		dut.put("D", 0.25d)
		dut.put("E", 0.25d)
		dut.put("F", 1.0d)

		/* Build the known cumulative distribution function */
		checkCdf.put("A", 0.25d)
		checkCdf.put("B", 0.5d)
		checkCdf.put("C", 0.625d)
		checkCdf.put("D", 0.6875d)
		checkCdf.put("E", 0.75d)
		checkCdf.put("F", 1.0d)
	}

	def "Test null input"() {
		when:
		new EmpiricalRandom<>(null)

		then:
		thrown(NullPointerException)
	}

	def "Check random seed"() {
		given:
		EmpiricalRandom random1 = new EmpiricalRandom(dut, 28032019l)
		EmpiricalRandom random2 = new EmpiricalRandom(dut, 28032019l)

		expect:
		random1.nextEmpirical() == random2.nextEmpirical()
	}

	def "Test empirical probability"() {
		given:
		EmpiricalRandom empiricalRandom = new EmpiricalRandom<>(dut, 28032019l)
		UnmodifiableMap<String, Double> cdf = empiricalRandom.getEmpiricalCdf()

		expect:
		cdf == checkCdf
	}
}
