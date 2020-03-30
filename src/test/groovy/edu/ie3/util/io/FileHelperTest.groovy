/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.io

import edu.ie3.test.common.TestFileCreator
import spock.lang.Specification

import java.nio.file.Files

/**
 * Tests the functionality of file helper class
 */
class FileHelperTest extends Specification {
	/**
	 * Create the folder and file test tree to be deleted with every test
	 * @return Nothing at all
	 */
	def setup(){
		TestFileCreator.createTestTree()
	}

	/**
	 * Finally clean up after all tests
	 * @return
	 */
	def cleanupSpec(){
		FileIOUtils.deleteRecursively(TestFileCreator.testFolderTree)
	}

	def "The whole test tree is deleted correctly"() {
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.testFolderTree)

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		!Files.exists(TestFileCreator.thirdLevel)
		!Files.exists(TestFileCreator.secondLevelFile)
		!Files.exists(TestFileCreator.secondLevel)
		!Files.exists(TestFileCreator.firstLevelFile)
		!Files.exists(TestFileCreator.testFolderTree)
	}

	def "The whole test tree is deleted correctly (based on the path's string)"() {
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.testFolderTree.toString())

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		!Files.exists(TestFileCreator.thirdLevel)
		!Files.exists(TestFileCreator.secondLevelFile)
		!Files.exists(TestFileCreator.secondLevel)
		!Files.exists(TestFileCreator.firstLevelFile)
		!Files.exists(TestFileCreator.testFolderTree)
	}

	def "The lowest level file is deleted correctly"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.thirdLevelFile)

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		Files.exists(TestFileCreator.thirdLevel)
		Files.exists(TestFileCreator.secondLevelFile)
		Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}

	def "The lowest level file is deleted correctly (based on the path's string)"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.thirdLevelFile.toString())

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		Files.exists(TestFileCreator.thirdLevel)
		Files.exists(TestFileCreator.secondLevelFile)
		Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}

	def "The lowest level folder is deleted correctly"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.thirdLevel)

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		!Files.exists(TestFileCreator.thirdLevel)
		Files.exists(TestFileCreator.secondLevelFile)
		Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}

	def "The lowest level folder is deleted correctly (based on the path's string)"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.thirdLevel.toString())

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		!Files.exists(TestFileCreator.thirdLevel)
		Files.exists(TestFileCreator.secondLevelFile)
		Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}

	def "The second level file is deleted correctly"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.secondLevelFile)

		then:
		Files.exists(TestFileCreator.thirdLevelFile)
		Files.exists(TestFileCreator.thirdLevel)
		!Files.exists(TestFileCreator.secondLevelFile)
		Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}

	def "The second level file is deleted correctly (based on the path's string)"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.secondLevelFile.toString())

		then:
		Files.exists(TestFileCreator.thirdLevelFile)
		Files.exists(TestFileCreator.thirdLevel)
		!Files.exists(TestFileCreator.secondLevelFile)
		Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}

	def "The second level folder is deleted correctly"(){
		when:
		FileIOUtils.deleteRecursively(TestFileCreator.secondLevel)

		then:
		!Files.exists(TestFileCreator.thirdLevelFile)
		!Files.exists(TestFileCreator.thirdLevel)
		!Files.exists(TestFileCreator.secondLevelFile)
		!Files.exists(TestFileCreator.secondLevel)
		Files.exists(TestFileCreator.firstLevelFile)
		Files.exists(TestFileCreator.testFolderTree)
	}
}
