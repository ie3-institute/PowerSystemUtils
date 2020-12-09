/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */
package edu.ie3.util.io

import edu.ie3.util.exceptions.FileException
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.io.FilenameUtils
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletionException
import java.util.stream.Collectors

class FileIOUtilsTest extends Specification {
	@Shared
	Path tmpDirectory

	def setup() {
		tmpDirectory = Files.createTempDirectory("psdm_fileio utils")
	}

	def cleanup() {
		FileIOUtils.deleteRecursively(tmpDirectory)
	}

	def "The fileio utils throws an exception, if the input path is null"() {
		given:
		def filePath = null
		def dirPath = null
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressFile(filePath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Input file name is null."

		when:
		def f2 = FileIOUtils.compressDir(dirPath, archiveFile)
		f2.join()

		then:
		ex = thrown(CompletionException)
		ex.message == "Input directory name is null."
	}

	def "The fileio utils throws an exception, if the function which compresses a file is sent a directory"() {
		given:
		def filePath = Paths.get(getClass().getResource('/testGridFiles/grid').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressFile(filePath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Input path '"+ filePath +"' is not of a valid file."
	}

	def "The fileio utils throws an exception, if the function which compresses a directory is sent a file"() {
		given:
		def dirPath = Paths.get(getClass().getResource('/testGridFiles/grid/node_input.csv').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressDir(dirPath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Input path '"+ dirPath +"' is not of a valid directory."
	}


	def "The fileio utils throws an exception, if the target file already exists"() {
		given:
		def filePath = Paths.get(getClass().getResource('/testGridFiles/grid/node_input.csv').toURI())
		def dirPath = Paths.get(getClass().getResource('/testGridFiles/grid').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))
		Files.createFile(archiveFile)

		when:
		def f1 = FileIOUtils.compressFile(filePath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "The target '" + archiveFile + "' already exists."

		when:
		def f2 = FileIOUtils.compressDir(dirPath, archiveFile)
		f2.join()

		then:
		def ex2 = thrown(CompletionException)
		ex2.message == "The target '" + archiveFile + "' already exists."
	}

	def "The fileio utils is able to zip one single file to .gz"() {
		given:
		def filePath = Paths.get(getClass().getResource('/testGridFiles/grid/node_input.csv').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressFile(filePath, archiveFile)
		f1.join()

		then:
		noExceptionThrown()
		Files.exists(archiveFile)
		Files.size(archiveFile) >= 500 && Files.size(archiveFile) <= 531 // Should be around 516 bytes +/- 3 %
	}

	def "The fileio utils is able to zip the contents of a directory to .tar.gz"() {
		given:
		def dirPath = Paths.get(getClass().getResource('/testGridFiles/grid').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressDir(dirPath, archiveFile)
		f1.join()

		then:
		noExceptionThrown()
		Files.exists(archiveFile)
		Files.size(archiveFile) >= 1330 && Files.size(archiveFile) <= 1412 // Should be around 1371 bytes +/- 3 %
	}

	def "The fileio utils is able to zip the contents of a directory with nested structure to .tar.gz"() {
		given:
		def dirPath = Paths.get(getClass().getResource('/testGridFiles/grid_default_hierarchy').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressDir(dirPath, archiveFile)
		f1.join()

		then:
		noExceptionThrown()
		Files.exists(archiveFile)
		Files.size(archiveFile) >= 1370 && Files.size(archiveFile) <= 1454 // Should be around 1412 bytes +/- 3 %
	}

	def  "The fileio utils throws an exception, if the archive to extract, is not apparent"() {
		given:
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "noFile.tar.gz"))
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		def ex = thrown(FileException)
		ex.message == "There is no archive '" + archiveFile + "' apparent."
	}

	def  "The fileio utils throws an exception, if the archive to extract, is a directory"() {
		given:
		def archiveFile = tmpDirectory
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		def ex = thrown(FileException)
		ex.message == "Archive '" + tmpDirectory + "' is not a file."
	}

	def  "The fileio utils throws an exception, if the archive to extract, does not end on '.tar.gz'"() {
		given:
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "someFile.txt"))
		Files.createFile(archiveFile)
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		def ex = thrown(FileException)
		ex.message == "Archive '" + archiveFile + "' does not end with '.tar.gz'."
	}

	def  "The fileio utils throws an exception, if the target folder already is available"() {
		given:
		def archiveFile = Paths.get(getClass().getResource('/default_directory_hierarchy.tar.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def targetPath = Paths.get(FilenameUtils.concat(targetDirectory.toString(), 'default_directory_hierarchy'))
		Files.createDirectories(targetPath)

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		def ex = thrown(FileException)
		ex.message == "The target path '" + targetPath + "' already exists."
	}

	def  "The fileio utils throws an exception, if a file exists at the target path"() {
		given:
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "default_directory_hierarchy.txt.tar.gz"))
		Files.createFile(archiveFile)
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def nestedTargetFolder = Paths.get(FilenameUtils.concat(targetDirectory.toString(), "default_directory_hierarchy.txt"))
		Files.createDirectories(targetDirectory)
		Files.createFile(nestedTargetFolder)

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		def ex = thrown(FileException)
		ex.message == "You intend to extract content of '" + archiveFile + "' to '" + nestedTargetFolder + "', which is a regular file."
	}

	def  "The fileio utils throws an exception, if the target folder already is available and filled"() {
		given:
		def archiveFile = Paths.get(getClass().getResource('/default_directory_hierarchy.tar.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def nestedTargetFolder = Paths.get(FilenameUtils.concat(targetDirectory.toString(), "default_directory_hierarchy"))
		Files.createDirectories(nestedTargetFolder)
		def oneFile = Paths.get(FilenameUtils.concat(nestedTargetFolder.toString(), "someFile.txt"))
		Files.createFile(oneFile)

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		def ex = thrown(FileException)
		ex.message == "The target path '" + nestedTargetFolder + "' already exists."
	}

	def  "The fileio utils is able to extract a tarball archive correctly"() {
		given:
		def archiveFile = Paths.get(getClass().getResource('/default_directory_hierarchy.tar.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		FileIOUtils.extract(archiveFile, targetDirectory)

		then:
		noExceptionThrown()
		Files.exists(targetDirectory)
		Files.list(targetDirectory).map { it.toString() }.sorted().collect(Collectors.toList()) == [
			tmpDirectory.toString() + "/extract/default_directory_hierarchy"
		]

		def nestedTargetDirectoryPath = Paths.get(FilenameUtils.concat(targetDirectory.toString(), "default_directory_hierarchy"))
		Files.exists(nestedTargetDirectoryPath)
		Files.list(nestedTargetDirectoryPath).map { it.toString() }.sorted().collect(Collectors.toList()) == [
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid",
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/participants"
		]

		def gridPath = Paths.get(FilenameUtils.concat(nestedTargetDirectoryPath.toString(), "grid"))
		Files.exists(gridPath)
		Files.list(gridPath).map { it.toString() }.sorted().collect(Collectors.toList()) == [
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid/line_input.csv",
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid/measurement_unit_input.csv",
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid/node_input.csv",
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid/switch_input.csv",
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid/transformer_2_w_input.csv",
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/grid/transformer_3_w_input.csv"
		]

		def participantsPath = Paths.get(FilenameUtils.concat(FilenameUtils.concat(targetDirectory.toString(), "default_directory_hierarchy"), "participants"))
		Files.exists(participantsPath)
		Files.list(participantsPath).map { it.toString() }.sorted().collect(Collectors.toList()) == [
			tmpDirectory.toString() + "/extract/default_directory_hierarchy/participants/ev_input.csv"
		]
	}

	def "The zip slip protection detects malicious entries correctly"() {
		given:
		def entry = Mock(ArchiveEntry)
		entry.name >> "../../pirates/home"

		when:
		FileIOUtils.zipSlipProtect(entry, tmpDirectory)

		then:
		def ex = thrown(IOException)
		ex.message == "Bad entry: ../../pirates/home"
	}
}
