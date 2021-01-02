/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io;

import edu.ie3.util.exceptions.FileException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Offers some useful features to simplify file handling. Inspired by:
 * https://stackoverflow.com/questions/779519/delete-directories-recursively-in-java/8685959#8685959
 * and by the linked MyKong Tutorial:
 *
 * @see <a href="https://mkyong.com/java/how-to-create-tar-gz-in-java/">MyKong Tutorial</a>
 */
public class FileIOUtils {
  private static final Logger logger = LoggerFactory.getLogger(FileIOUtils.class);

  private static final String GZ = ".gz";
  private static final String TARGZ = ".tar.gz";
  private static final String TO = "' to '";
  private static final String ALREADY_EXISTS = "' already exists.";
  public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
  public static final Charset CHARSET_WINDOWS_ISO88591 = StandardCharsets.ISO_8859_1;
  public static final String NATIVE_NEWLINE = System.getProperty("line.separator");
  private static final int MAX_AMOUNT_OF_ENTRIES = 5000;
  private static final long MAX_SIZE_UNCOMPRESSED = 0x280000000L; // 10 GB
  private static final double MAX_COMPRESSION_RATIO = 0.75;

  private FileIOUtils() {
    throw new IllegalStateException("This is a Utility Class and not meant to be instantiated.");
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
   * @deprecated replaced by #compressFile(Path, Path). Add ".gz" to the input path and pass it as
   *     output filepath in compressFile() for a similar functionality.
   */
  @Deprecated
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
   * @deprecated replaced by #compressFile(Path, Path)
   */
  @Deprecated
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
   * Compress all the files present in the provided directory and returns a {@link Future} with the
   * result.
   *
   * @param dirName path of the directory that should be compressed
   * @param outputFileName path of the output file where the compressed content will be stored (path
   *     should be for a file and filename should have a ".tar.gz" extension)
   * @return a Future containing a boolean which is either true on success or false otherwise
   */
  public static CompletableFuture<Boolean> compressDir(
      final Path dirName, final Path outputFileName) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            File validatedInputDir = validateInputDirName(dirName);
            File validatedOutputFile =
                validateOutputFileName(
                    validatedInputDir.toString(), outputFileName.toString(), true);
            return compressDir(validatedInputDir.toPath(), validatedOutputFile);
          } catch (FileException e) {
            throw new CompletionException(
                "Cannot compress directory '" + dirName + TO + outputFileName + "'.", e);
          }
        });
  }

  /**
   * Checks, if the input file path points to a valid directory and returns the corresponding File
   * object.
   *
   * @param dirName path of the directory that should be compressed
   * @return File object corresponding to the directory to be compressed
   * @throws FileException If the input path is null or the input path is not of a valid directory
   */
  private static File validateInputDirName(final Path dirName) throws FileException {
    if (dirName == null) {
      throw new FileException("Input directory name is null.");
    }

    File inputDir = new File(dirName.toString());

    if (!inputDir.isDirectory()) {
      throw new FileException(
          "Input path '" + dirName.toString() + "' is not of a valid directory.");
    }

    return inputDir;
  }

  /**
   * Checks, if the output file path actually ends with '.tar.gz' or '.gz'. If not, creates a
   * filename from the directory or file name. Also, checks if the target file already exists. If it
   * does not exist, finally creates the file.
   *
   * @param dirOrFileName path of the directory or file that should be compressed (full path
   *     required in case of a directory / full path + filename + file extension required in case of
   *     a file!)
   * @param outputFileName the optional output filename (full path + filename + file extension
   *     required!) if different from the provided filename
   * @param isDir set to true if trying to compress a directory. Set to false if compressing a
   *     single file. if set to true, will archive the files before compression
   * @return Output file
   * @throws FileException If the archive already exists or unable to create the output file
   */
  private static File validateOutputFileName(
      final String dirOrFileName, final String outputFileName, boolean isDir) throws FileException {
    String finalOutputFileName = null;

    if (isDir) {
      finalOutputFileName = outputFileName.endsWith(TARGZ) ? outputFileName : dirOrFileName + TARGZ;
    } else {
      finalOutputFileName = outputFileName.endsWith(GZ) ? outputFileName : dirOrFileName + GZ;
    }

    File outputFile = new File(finalOutputFileName);
    if (outputFile.exists())
      throw new FileException("The target '" + finalOutputFileName + ALREADY_EXISTS);

    try {
      if (!outputFile.createNewFile())
        throw new FileException("Cannot create file '" + finalOutputFileName + "'.");
    } catch (IOException e) {
      throw new FileException("Cannot create file '" + finalOutputFileName + "'.", e);
    }

    return outputFile;
  }

  /**
   * Compress all the files present in the provided directory and returns a boolean value denoting
   * success or failure.
   *
   * @param dirName path of the directory that should be compressed
   * @param validatedOutputFile validated output file (target archive file)
   * @return a boolean which is either true on success or false otherwise
   * @throws FileException If unable to write to the output stream
   */
  private static boolean compressDir(final Path dirName, File validatedOutputFile)
      throws FileException {
    Path validatedOutputFileName;

    validatedOutputFileName = validatedOutputFile.toPath();

    /* Open a stream and add content to the archive */
    try (BufferedOutputStream bufferedOutputStream =
            new BufferedOutputStream(Files.newOutputStream(validatedOutputFileName));
        GzipCompressorOutputStream gzipOutputStream =
            new GzipCompressorOutputStream(bufferedOutputStream);
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(gzipOutputStream)) {
      Files.walkFileTree(
          dirName,
          new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
              /* Skip symbolic link */
              if (attrs.isSymbolicLink()) return FileVisitResult.CONTINUE;

              /* Copy files to archive */
              Path relFile = dirName.relativize(file);
              TarArchiveEntry entry = new TarArchiveEntry(file.toFile(), relFile.toString());
              tarOutputStream.putArchiveEntry(entry);
              Files.copy(file, tarOutputStream);
              tarOutputStream.closeArchiveEntry();

              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
              logger.error(
                  "Unable to add '{}' to '{}'. Stopping compression.",
                  file,
                  validatedOutputFileName,
                  exc);
              return FileVisitResult.TERMINATE;
            }
          });

      /* Close everything properly */
      tarOutputStream.finish();
      return true;
    } catch (IOException e) {
      throw new FileException("Unable to write to '" + validatedOutputFileName + "'.", e);
    }
  }

  /**
   * Compresses a single file and returns a {@link Future} with the result.
   *
   * @param fileName path of the file that should be compressed
   * @param outputFileName path of the output file where the compressed content will be stored (path
   *     should be for a file and filename should have a ".gz" extension)
   * @return a Future containing a boolean which is either true on success or false otherwise
   */
  public static CompletableFuture<Boolean> compressFile(
      final Path fileName, final Path outputFileName) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            File validatedInputFile = validateInputFileName(fileName);
            File validatedOutputFile =
                validateOutputFileName(
                    validatedInputFile.toString(), outputFileName.toString(), false);

            return compressFile(validatedInputFile, validatedOutputFile);

          } catch (FileException e) {
            throw new CompletionException(
                "Cannot compress file '" + fileName + TO + outputFileName + "'.", e);
          }
        });
  }

  /**
   * Checks, if the input file path points to a valid file and returns an object of the input file.
   *
   * @param fileName path of the file that should be compressed
   * @return File to be compressed
   * @throws FileException If the input file name is null or the input path is not of a valid file
   */
  private static File validateInputFileName(final Path fileName) throws FileException {
    if (fileName == null) {
      throw new FileException("Input file name is null.");
    }

    File inputFile = new File(fileName.toString());

    if (!inputFile.isFile()) {
      throw new FileException("Input path '" + fileName.toString() + "' is not of a valid file.");
    }

    return inputFile;
  }

  /**
   * Compresses a single file and returns a boolean with the result.
   *
   * @param validatedInputFile validated input file that should be compressed
   * @param validatedOutputFile validated output file where the compressed content will be stored
   * @return a boolean which is either true on success or false otherwise
   */
  private static boolean compressFile(
      final File validatedInputFile, final File validatedOutputFile) {
    try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(validatedOutputFile))) {
      try (FileInputStream in = new FileInputStream(validatedInputFile)) {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
          out.write(buffer, 0, len);
        }
      }
    } catch (IOException e) {
      logger.error(
          "Unable to write from file '{}' to file '{}'",
          validatedInputFile,
          validatedOutputFile,
          e);
      return false;
    }
    return true;
  }

  /**
   * Extracts the given archive to a sub-directory with the same name that the archive has beneath
   * the target directory.
   *
   * @param archive Compressed tarball archive to extract
   * @param target Path to the target folder
   * @return a Future containing a path to the actual folder, where the content is extracted to
   */
  public static CompletableFuture<Path> extractDir(Path archive, Path target) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            /* Pre-flight checks and assembly of the target path */
            Path targetDirectory = determineTargetDirectory(archive, target);

            /* Get the archive file size */
            long archiveSize = archive.toFile().length();

            /* Create the target folder */
            try {
              Files.createDirectories(targetDirectory);
            } catch (IOException e) {
              throw new FileException(
                  "Cannot create target directory '" + targetDirectory + "'.", e);
            }

            /* Monitor amount of entries and their size for safety reasons */
            int entries = 0;
            long size = 0;
            try (InputStream fileInputStream = Files.newInputStream(archive);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                GzipCompressorInputStream gzipInputStream =
                    new GzipCompressorInputStream(bufferedInputStream);
                TarArchiveInputStream tarInputStream = new TarArchiveInputStream(gzipInputStream)) {
              ArchiveEntry archiveEntry;
              while ((archiveEntry = tarInputStream.getNextEntry()) != null) {
                /* Control the total amount of entries */
                entries++;
                if (entries > MAX_AMOUNT_OF_ENTRIES)
                  throw new IOException(
                      "The archive contains too many entries and is therefore possibly malicious.");

                /* Control the size of extracted archive files */
                long uncompressedSize = archiveEntry.getSize();
                if (uncompressedSize == ArchiveEntry.SIZE_UNKNOWN)
                  throw new IOException(
                      "Unknown uncompressed file size of '" + archiveEntry.getName() + "'");
                size += uncompressedSize;
                if (size > MAX_SIZE_UNCOMPRESSED)
                  throw new IOException(
                      "Uncompressed size of archive exceeds permissible "
                          + (MAX_SIZE_UNCOMPRESSED / 1024 / 1024)
                          + " MB. Possibly malicious archive");

                /* Control the compression ratio */
                if (1 - (double) archiveSize / size > MAX_COMPRESSION_RATIO)
                  throw new IOException(
                      "Compression ratio exceeds its maximum permissible value "
                          + (MAX_COMPRESSION_RATIO * 100)
                          + " %. Possibly malicious archive");

                handleZipEntrySafely(archiveEntry, targetDirectory, tarInputStream);
              }
            } catch (IOException ex) {
              throw new FileException("Unable to extract from '" + archive + "'.", ex);
            }

            return targetDirectory;
          } catch (FileException e) {
            throw new CompletionException(
                "Unable to extract from archive '" + archive + "' to target '" + target + "'.", e);
          }
        });
  }

  /**
   * Runs some pre-flight checks and assembles the target directory
   *
   * @param archive Compressed tarball archive to extract
   * @param target Path to the target folder
   * @return Path to the folder, where the content is meant to be extracted to
   * @throws FileException If the pre-flight checks fail
   */
  private static Path determineTargetDirectory(Path archive, Path target) throws FileException {
    /* Pre-flight checks */
    if (Files.notExists(archive))
      throw new FileException("There is no archive '" + archive + "' apparent.");
    if (!Files.isRegularFile(archive))
      throw new FileException("Archive '" + archive + "' is not a file.");
    if (!archive.toString().endsWith(TARGZ))
      throw new FileException("Archive '" + archive + "' does not end with '" + TARGZ + "'.");

    /* Determine the file name */
    String fileName = archive.getFileName().toString().replaceAll("\\.tar\\.gz$", "");
    Path targetDirectory = Paths.get(FilenameUtils.concat(target.toString(), fileName));

    /* Some more pre-flight checks */
    if (Files.exists(targetDirectory)) {
      if (Files.isRegularFile(targetDirectory))
        throw new FileException(
            "You intend to extract content of '"
                + archive
                + TO
                + targetDirectory
                + "', which is a regular file.");
      else throw new FileException("The target path '" + targetDirectory + ALREADY_EXISTS);
    }

    return targetDirectory;
  }

  /**
   * Handles the "copying" of the zip entry's content to actual files on the hard drive.
   *
   * @param archiveEntry Entry to be treated
   * @param targetDirectory Path to the target folder
   * @param tarInputStream Input stream
   * @throws IOException Whenever something does not work
   */
  private static void handleZipEntrySafely(
      ArchiveEntry archiveEntry, Path targetDirectory, TarArchiveInputStream tarInputStream)
      throws IOException {
    /* Check against zip slip vulnerability and return normalized path w.r.t. the target path */
    Path targetEntryPath = zipSlipProtect(archiveEntry, targetDirectory);

    if (archiveEntry.isDirectory()) {
      Files.createDirectories(targetEntryPath);
    } else {
      /* Check, if parent folder is apparent, otherwise create it */
      Path parentDirectoryPath = targetEntryPath.getParent();
      if (parentDirectoryPath != null && Files.notExists(parentDirectoryPath)) {
        Files.createDirectories(parentDirectoryPath);
      }

      /* Copy content to new path */
      Files.copy(tarInputStream, targetEntryPath, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  /**
   * Offers protection against zip slip vulnerability, by making sure, that the target entry still
   * contains the target directory. If everything is fine, the normalized path (w.r.t. the target
   * path) is handed back.
   *
   * @param entry Entry to be extracted
   * @param targetDir Path to the target directory
   * @return Normalized path w.r.t. the target path
   * @throws IOException If the entry may impose zip slip danger
   * @see <a href="https://snyk.io/research/zip-slip-vulnerability">Snyk.io vulnerability
   *     description</a>
   * @see <a href="https://mkyong.com/java/how-to-create-tar-gz-in-java/">MyKong Tutorial</a>
   */
  private static Path zipSlipProtect(ArchiveEntry entry, Path targetDir) throws IOException {
    Path targetDirResolved = targetDir.resolve(entry.getName());
    Path normalizePath = targetDirResolved.normalize();

    if (!normalizePath.startsWith(targetDir)) {
      throw new IOException("Bad entry: " + entry.getName());
    }

    return normalizePath;
  }

  /**
   * Extracts the given zipped file to a file with the same name that the zipped file has beneath
   * the target directory.
   *
   * @param zippedFile Compressed gzip file to extract
   * @param target Path to the target folder
   * @return a Future containing a path of the file, where the content is extracted to
   * @see <a href="https://mkyong.com/java/how-to-decompress-file-from-gzip-file/">MyKong
   *     Tutorial</a>
   */
  public static CompletableFuture<Path> extractFile(Path zippedFile, Path target) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            /* Pre-flight checks and assembly of the target path */
            validateZippedFile(zippedFile);
            Path targetPath = validateTargetFile(zippedFile, target);

            /* Get the zipped file size */
            long zippedSize = zippedFile.toFile().length();

            try (InputStream fileInputStream = new FileInputStream(zippedFile.toFile());
                GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream); ) {

              long uncompressedSize = 0;

              File outputFile = targetPath.toFile();

              try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile); ) {

                /* Copy the contents of the gzip input stream to the file output stream while performing size checks */
                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipInputStream.read(buffer)) > 0) {
                  fileOutputStream.write(buffer, 0, len);
                  /* Monitor uncompressed size for safety reasons */
                  uncompressedSize += len;
                  if (uncompressedSize > MAX_SIZE_UNCOMPRESSED) {
                    Files.delete(outputFile.toPath());
                    throw new IOException(
                        "Uncompressed size of zipped file exceeds permissible "
                            + (MAX_SIZE_UNCOMPRESSED / 1024 / 1024)
                            + " MB. Possibly malicious file");
                  }

                  /* Control the compression ratio */
                  if (1 - (double) zippedSize / uncompressedSize > MAX_COMPRESSION_RATIO) {
                    Files.delete(outputFile.toPath());
                    throw new IOException(
                        "Compression ratio exceeds its maximum permissible value "
                            + (MAX_COMPRESSION_RATIO * 100)
                            + " %. Possibly malicious file");
                  }
                }
              }
            } catch (IOException ex) {
              throw new FileException(
                  "Unable to extract from '" + zippedFile + TO + targetPath + "'.", ex);
            }

            return targetPath;
          } catch (FileException e) {
            throw new CompletionException(
                "Unable to extract from zipped file '"
                    + zippedFile
                    + "' to target '"
                    + target
                    + "'.",
                e);
          }
        });
  }

  /**
   * Runs some pre-flight checks for the zipped file to be extracted
   *
   * @param zippedFile Compressed gzip file to extract
   * @throws FileException If the pre-flight checks fail
   */
  private static void validateZippedFile(Path zippedFile) throws FileException {
    /* Pre-flight checks */
    if (Files.notExists(zippedFile))
      throw new FileException("There is no zipped file '" + zippedFile + "' apparent.");
    if (!Files.isRegularFile(zippedFile))
      throw new FileException("'" + zippedFile + "' is not a regular file.");
    if (!zippedFile.toString().endsWith(GZ))
      throw new FileException("Zipped file '" + zippedFile + "' does not end with '" + GZ + "'.");
  }

  /**
   * Runs some pre-flight checks and assembles the target path
   *
   * @param zippedFile Compressed gzip file to extract
   * @param targetDir Path to the target directory
   * @return Path to the folder, where the content is meant to be extracted to
   * @throws FileException If the target file cannot be created
   */
  private static Path validateTargetFile(Path zippedFile, Path targetDir) throws FileException {
    /* Determine the file name */
    String fileName = zippedFile.getFileName().toString().replaceAll("\\.gz$", "");
    Path targetPath = Paths.get(FilenameUtils.concat(targetDir.toString(), fileName));

    /* Some pre-flight checks */
    if (Files.exists(targetPath)) {
      if (!Files.isRegularFile(targetPath))
        throw new FileException(
            "You intend to extract content of '"
                + zippedFile
                + TO
                + targetPath
                + "', which is a directory.");
      else throw new FileException("The target file '" + targetPath + ALREADY_EXISTS);
    }

    /* Create the destination folder for unzipping the zip file */
    try {
      Path parentDirectoryPath = targetPath.getParent();
      if (parentDirectoryPath != null) {
        if (Files.notExists(parentDirectoryPath)) {
          Files.createDirectories(parentDirectoryPath);
        }
      } else {
        throw new FileException("Parent directory path is null for the file '" + targetPath + "'.");
      }
    } catch (IOException e) {
      throw new FileException("Cannot create target folder for the file '" + targetPath + "'.", e);
    }

    /*Create the output file for extracting contents of the zipped file*/
    File outputFile = new File(targetPath.toString());
    try {
      if (!outputFile.createNewFile())
        throw new FileException("Cannot create target output file '" + outputFile + "'.");
    } catch (IOException e) {
      throw new FileException("Cannot create target output file '" + outputFile + "'.", e);
    }

    return targetPath;
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
            try {
              Files.delete(Paths.get(f.getAbsolutePath()));
              return true;
            } catch (IOException e) {
              logger.error("Unable to delete file '{}'.", f, e);
              return false;
            }
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
