/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Offers some useful features to simplify file handling. Inspired by
 * https://stackoverflow.com/questions/779519/delete-directories-recursively-in-java/8685959#8685959
 */
public class FileIOUtils {
  private static final Logger logger = LoggerFactory.getLogger(FileIOUtils.class);

  private static final String GZ = ".gz";

  public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
  public static final Charset CHARSET_WINDOWS_ISO88591 = StandardCharsets.ISO_8859_1;

  public static final String NATIVE_NEWLINE = System.getProperty("line.separator");

  private FileIOUtils() {
    throw new IllegalStateException("Do not instantiate an utility class.");
  }

  /**
   * Tries to open the specified file for writing and returns a UTF-8 charset encoded BufferedWriter
   * for it. If the filename ends with ".gz", data will be automatically gzip-compressed.
   *
   * @param filename The filename where to write the data.
   * @return BufferedWriter for the specified file.
   * @throws IOException
   */
  public static BufferedWriter getBufferedWriterUTF8(final String filename) throws IOException {
    return getBufferedWriter(filename, CHARSET_UTF8);
  }

  /**
   * Tries to open the specified file for writing and returns a BufferedWriter for it. If the
   * filename ends with ".gz", data will be automatically gzip-compressed.
   *
   * @param filename The filename where to write the data.
   * @param charset the encoding to use to write the file.
   * @return BufferedWriter for the specified file.
   * @throws IOException
   */
  public static BufferedWriter getBufferedWriter(final String filename, final Charset charset)
      throws IOException {
    return getBufferedWriter(filename, charset, false);
  }

  /**
   * Tries to open the specified file for writing and returns a BufferedWriter for it. If the
   * filename ends with ".gz", data will be automatically gzip-compressed.
   *
   * @param filename filename and path where to write the data e.g. /tmp/out/sample.csv
   * @param charset the required file encoding the while should be written in
   * @param append <code>true</code> if the file should be opened for appending, instead of
   *     overwriting - only works if filename doesn't end with .gz
   * @return BufferedWriter for the specified file
   * @throws IOException
   */
  public static BufferedWriter getBufferedWriter(
      final String filename, final Charset charset, final boolean append) throws IOException {
    if (filename == null) {
      throw new IOException(new FileNotFoundException("No filename given (filename == null)"));
    }
    try {
      if (filename.toLowerCase(Locale.ROOT).endsWith(GZ)) {
        File f = new File(filename);
        if (append && f.exists() && (f.length() > 0)) {
          throw new IllegalArgumentException(
              "Appending to an existing gzip-compressed file is not supported.");
        }
        return new BufferedWriter(
            new OutputStreamWriter(
                new GZIPOutputStream(new FileOutputStream(filename, append)), charset));
      }
      return new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(filename, append), charset));
    } catch (IOException e) {
      throw new IOException(e);
    }
  }

  /**
   * Compress the provided file (full path + filename required! e.g. /tmp/out/sample.csv) and
   * returns a {@link Future} with the result.
   *
   * @param filename the file that should be compress (full path + filename + file extension
   *     required!)
   * @return a Future containing a boolean which is either true on success or false otherwise
   */
  public static CompletableFuture<Boolean> gzip(final String filename) {
    return gzip(filename, "");
  }

  /**
   * Compress the provided file (full path + filename required! e.g. /tmp/out/sample.csv) and
   * returns a {@link Future} with the result.
   *
   * @param filename the file that should be compress (full path + filename + file extension
   *     required!)
   * @param outputFileName the optional output filename (full path + filename + file extension
   *     required!) if different from the provided filename
   * @return a Future containing a boolean which is either true on success or false otherwise
   */
  public static CompletableFuture<Boolean> gzip(
      final String filename, final String outputFileName) {
    return CompletableFuture.supplyAsync(
        () -> {
          String outputFile = outputFileName.endsWith(".gz") ? outputFileName : filename + ".gz";
          try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outputFile))) {
            try (FileInputStream in = new FileInputStream(filename)) {
              byte[] buffer = new byte[1024];
              int len;
              while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
              }
            }
          } catch (IOException e) {
            return false;
          }
          return true;
        });
  }

  /**
   * Checks if a file exists and if yes, the file will be deleted and the Future will return true,
   * otherwise it will return false
   *
   * @param filename the file that should be deleted (full path + filename)
   * @return true if the file has been deleted, false otherwise
   */
  public static CompletableFuture<Boolean> deleteFileIfExists(final String filename) {
    return CompletableFuture.supplyAsync(
        () -> {
          File f = new File(filename);
          if (f.exists()) {
            return f.delete();
          } else {
            return false;
          }
        });
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
