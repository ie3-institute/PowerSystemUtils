/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.test.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Creates hierarchically nested folders and files */
public class TestFileCreator {
  private TestFileCreator() {
    throw new IllegalStateException("Don't try to initialize a utility class.");
  }

  public static final Path testFolderTree = Paths.get("testFolderTree");
  public static final Path firstLevelFile = Paths.get("testFolderTree/firstLevelFile.txt");
  public static final Path secondLevel = Paths.get("testFolderTree/secondLevel");
  public static final Path secondLevelFile =
      Paths.get("testFolderTree/secondLevel/secondLevelFile.txt");
  public static final Path thirdLevel = Paths.get("testFolderTree/secondLevel/thirdLevel");
  public static final Path thirdLevelFile =
      Paths.get("testFolderTree/secondLevel/thirdLevel/thirdLevelFile.txt");

  /**
   * Creates the whole test folder and file tree
   *
   * @throws IOException If any folder or file cannot be created
   */
  public static void createTestTree() throws IOException {
    if (!thirdLevel.toFile().exists()) Files.createDirectories(thirdLevel);
    if (!thirdLevelFile.toFile().exists()) Files.createFile(thirdLevelFile);
    if (!secondLevelFile.toFile().exists()) Files.createFile(secondLevelFile);
    if (!firstLevelFile.toFile().exists()) Files.createFile(firstLevelFile);
  }
}
