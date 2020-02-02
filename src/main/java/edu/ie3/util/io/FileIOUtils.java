/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Offers some useful features to simplify file handling. Inspired by
 * https://stackoverflow.com/questions/779519/delete-directories-recursively-in-java/8685959#8685959
 */
public class FileIOUtils {
  private static final Logger logger = LoggerFactory.getLogger(FileIOUtils.class);

  private FileIOUtils() {
    throw new IllegalStateException("Do not instantiate an utility class.");
  }

  /**
   * Deletes the given path described by the {@code path}-string recursively, including the path
   * itself as well
   *
   * @param path The path to delete
   * @throws IOException If something goes wrong.
   */
  public static void deleteRecursively(String path) throws IOException {
    deleteRecursively(Paths.get(path));
  }

  /**
   * Deletes the given {@code path} recursively, including the path itself as well
   *
   * @param path The path to delete
   * @throws IOException If something goes wrong.
   */
  public static void deleteRecursively(Path path) throws IOException {
    Files.walkFileTree(
        path,
        new SimpleFileVisitor<Path>() {
          /**
           * What to do, when a file is visited
           *
           * @param file The visited file
           * @param attrs Its attributes
           * @return What to do next
           * @throws IOException If something goes wrong
           */
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            logger.debug("Deleting file {}", file);
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }

          /**
           * What to to, if the visit of the file fails. Might be a rare case, that anything can be
           * done, but it could happen, that the user is not allowed to assess the file, but delete
           * it.
           *
           * @param file The visited file
           * @param exc What went wrong during attempt to visit
           * @return What to do next
           * @throws IOException If something goes wrong
           */
          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            Files.delete(file);
            logger.debug("Deleting file {}, after unsuccessfully visiting it.", file);
            return FileVisitResult.CONTINUE;
          }

          /**
           * What to do, after a directory was visited. If the visit of the directory failed,
           * propagate the exception.
           *
           * @param dir The directory
           * @param exc Exception that might have occurred during directory visit
           * @return What to do next
           * @throws IOException If something goes wrong
           */
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if (exc == null) {
              logger.debug("Deleting directory {}", dir);
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            } else {
              logger.debug(
                  "Visiting of directory {} failed wit exception. Propagate it.", dir, exc);
              throw exc;
            }
          }
        });
  }
}
