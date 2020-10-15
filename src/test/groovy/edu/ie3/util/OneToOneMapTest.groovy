/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util

import spock.lang.Specification


class OneToOneMapTest extends Specification {

	def "A OneToOneMap implementation should work as expected"() {
		given:
		def oneToOneMap = new OneToOneMap<String, String>()

		expect:
		oneToOneMap.size() == 0
		oneToOneMap.put("Key", "Value") == null
		oneToOneMap.put("Key", "Value") == "Value"

		oneToOneMap.size() == 1
		oneToOneMap.get("Key") == "Value"
		oneToOneMap.getKey("Value") == "Key"

		oneToOneMap.removeValue("Value") == "Key"
		oneToOneMap.size() == 0

		oneToOneMap.put("Key", "Value") == null
		oneToOneMap.removeValue("Value") == "Key"
		oneToOneMap.size() == 0
	}
}
