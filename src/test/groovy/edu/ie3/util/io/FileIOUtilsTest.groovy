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
		tmpDirectory = Files.createTempDirectory("psu_fileio utils")
	}

	def cleanup() {
		FileIOUtils.deleteRecursively(tmpDirectory)
	}

	def "The fileio utils throws an exception, if the input path is null when called to compress a directory"() {
		given:
		def dirPath = null
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressDir(dirPath, archiveFile)
		f1.join()

		then:
		CompletionException ex = thrown(CompletionException)
		ex.message == "Cannot compress directory '" + dirPath + "' to '" + archiveFile + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Input directory name is null."
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
		ex.message == "Cannot compress directory '" + dirPath + "' to '" + archiveFile + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Input path '" + dirPath + "' is not of a valid directory."
	}

	def "The fileio utils throws an exception, if the target file already exists while compressing a directory"() {
		given:
		def dirPath = Paths.get(getClass().getResource('/testGridFiles/grid').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))
		Files.createFile(archiveFile)

		when:
		def f1 = FileIOUtils.compressDir(dirPath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Cannot compress directory '" + dirPath + "' to '" + archiveFile + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "The target '" + archiveFile + "' already exists."
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
		Files.size(archiveFile) >= 1511 && Files.size(archiveFile) <= 1605 // Should be around 1558 bytes +/- 3 %
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
		Files.size(archiveFile) >= 1578 && Files.size(archiveFile) <= 1676 // Should be around 1627 bytes +/- 3 %
	}

	def "The fileio utils throws an exception, if the input path is null when called to compress a file"() {
		given:
		def filePath = null
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))

		when:
		def f1 = FileIOUtils.compressFile(filePath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Cannot compress file '" + filePath + "' to '" + archiveFile + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Input file name is null."
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
		ex.message == "Cannot compress file '" + filePath + "' to '" + archiveFile + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Input path '" + filePath + "' is not of a valid file."
	}

	def "The fileio utils throws an exception, if the target file already exists while compressing a file"() {
		given:
		def filePath = Paths.get(getClass().getResource('/testGridFiles/grid/node_input.csv').toURI())
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "test.tar.gz"))
		Files.createFile(archiveFile)

		when:
		def f1 = FileIOUtils.compressFile(filePath, archiveFile)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Cannot compress file '" + filePath + "' to '" + archiveFile + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "The target '" + archiveFile + "' already exists."
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

	def  "The fileio utils throws an exception, if the archive to extract, is not apparent"() {
		given:
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "noFile.tar.gz"))
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from archive '" + archiveFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "There is no archive '" + archiveFile + "' apparent."
	}

	def  "The fileio utils throws an exception, if the archive to extract, is a directory"() {
		given:
		def archiveFile = tmpDirectory
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from archive '" + archiveFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Archive '" + archiveFile + "' is not a file."
	}

	def  "The fileio utils throws an exception, if the archive to extract, does not end on '.tar.gz'"() {
		given:
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "someFile.txt"))
		Files.createFile(archiveFile)
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from archive '" + archiveFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Archive '" + archiveFile + "' does not end with '.tar.gz'."
	}

	def  "The fileio utils throws an exception, if the target folder already is available while extracting a '.tar.gz' file"() {
		given:
		def archiveFile = Paths.get(getClass().getResource('/zippedFiles/default_directory_hierarchy.tar.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def targetPath = Paths.get(FilenameUtils.concat(targetDirectory.toString(), 'default_directory_hierarchy'))
		Files.createDirectories(targetPath)

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from archive '" + archiveFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "The target path '" + targetPath + "' already exists."
	}

	def  "The fileio utils throws an exception, if a file exists at the target path while extracting a '.tar.gz' file"() {
		given:
		def archiveFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "default_directory_hierarchy.txt.tar.gz"))
		Files.createFile(archiveFile)
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def nestedTargetFolder = Paths.get(FilenameUtils.concat(targetDirectory.toString(), "default_directory_hierarchy.txt"))
		Files.createDirectories(targetDirectory)
		Files.createFile(nestedTargetFolder)

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from archive '" + archiveFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "You intend to extract content of '" + archiveFile + "' to '" + nestedTargetFolder + "', which is a regular file."
	}

	def  "The fileio utils throws an exception, if the target folder already is available and filled while extracting a '.tar.gz' file"() {
		given:
		def archiveFile = Paths.get(getClass().getResource('/zippedFiles/default_directory_hierarchy.tar.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def nestedTargetFolder = Paths.get(FilenameUtils.concat(targetDirectory.toString(), "default_directory_hierarchy"))
		Files.createDirectories(nestedTargetFolder)
		def oneFile = Paths.get(FilenameUtils.concat(nestedTargetFolder.toString(), "someFile.txt"))
		Files.createFile(oneFile)

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from archive '" + archiveFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "The target path '" + nestedTargetFolder + "' already exists."
	}

	def  "The fileio utils is able to extract a tarball archive correctly"() {
		given:
		def archiveFile = Paths.get(getClass().getResource('/zippedFiles/default_directory_hierarchy.tar.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractDir(archiveFile, targetDirectory)
		f1.join()

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

	def  "The fileio utils throws an exception, if the zipped file to extract, is not apparent"() {
		given:
		def zippedFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "noFile.gz"))
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractFile(zippedFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from zipped file '" + zippedFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "There is no zipped file '" + zippedFile + "' apparent."
	}

	def  "The fileio utils throws an exception, if the zipped file to extract, is a directory"() {
		given:
		def zippedFile = tmpDirectory
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractFile(zippedFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from zipped file '" + zippedFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "'" + zippedFile + "' is not a regular file."
	}

	def  "The fileio utils throws an exception, if the zipped file to extract, does not end on '.gz'"() {
		given:
		def zippedFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "someFile.txt"))
		Files.createFile(zippedFile)
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))

		when:
		def f1 = FileIOUtils.extractFile(zippedFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from zipped file '" + zippedFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "Zipped file '" + zippedFile + "' does not end with '.gz'."
	}

	def  "The fileio utils throws an exception, if a file with the same name as the target file already exists while extracting a zipped file"() {
		given:
		def zippedFile = Paths.get(getClass().getResource('/zippedFiles/line_input.csv.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def alreadyPresentFile = Paths.get(FilenameUtils.concat(targetDirectory.toString(), "line_input.csv"))
		Files.createDirectories(targetDirectory)
		Files.createFile(alreadyPresentFile)

		when:
		def f1 = FileIOUtils.extractFile(zippedFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from zipped file '" + zippedFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "The target file '" + alreadyPresentFile + "' already exists."
	}

	def  "The fileio utils throws an exception, if a directory exists at the target file path while extracting a zipped file"() {
		given:
		def zippedFile = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "line_input.csv.gz"))
		Files.createFile(zippedFile)
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def alreadyPresentFile = Paths.get(FilenameUtils.concat(targetDirectory.toString(), 'line_input.csv'))
		Files.createDirectories(alreadyPresentFile)

		when:
		def f1 = FileIOUtils.extractFile(zippedFile, targetDirectory)
		f1.join()

		then:
		def ex = thrown(CompletionException)
		ex.message == "Unable to extract from zipped file '" + zippedFile + "' to target '" + targetDirectory + "'."
		ex.cause.getClass() == FileException
		ex.cause.message == "You intend to extract content of '" + zippedFile + "' to '" + alreadyPresentFile + "', which is a directory."
	}

	def  "The fileio utils is able to unzip a zipped file correctly"() {
		given:
		def zippedFile = Paths.get(getClass().getResource('/zippedFiles/line_input.csv.gz').toURI())
		def targetDirectory = Paths.get(FilenameUtils.concat(tmpDirectory.toString(), "extract"))
		def testFile = Paths.get(getClass().getResource('/testGridFiles/grid_default_hierarchy/grid/line_input.csv').toURI())

		when:
		def f1 = FileIOUtils.extractFile(zippedFile, targetDirectory)
		def unzippedFile = f1.join()

		then:
		noExceptionThrown()
		Files.exists(unzippedFile)
		Files.list(targetDirectory).map { it.toString() }.sorted().collect(Collectors.toList()) == [
			tmpDirectory.toString() + "/extract/line_input.csv"
		]
		/* Check unzipped file size */
		Files.size(unzippedFile) == Files.size(testFile)
	}
}
