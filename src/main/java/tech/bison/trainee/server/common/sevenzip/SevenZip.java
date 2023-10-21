package tech.bison.trainee.server.common.sevenzip;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;

public class SevenZip {
  private static final Logger LOGGER = Logger.getLogger(SevenZip.class.getName());

  public static final String SEVEN_ZIP_FILE_ENDING = ".7z";
  public static final int MEGABYTE = 1_000_000;


  public double compress(File input, File archive) throws IOException {
    final long originalSize = input.length();
    final ProcessBuilder processBuilder = new ProcessBuilder("7z", "a", "-t7z", "-mx=9", archive.getAbsolutePath(),
        input.getAbsolutePath());
    try {
      execute(processBuilder);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw new IOException("7z compression process was interrupted", e);
    }
    final long compressedSize = archive.length();
    return ((double) (originalSize - compressedSize) / MEGABYTE);
  }

  public double extractTo(File archive, File extractionDir) throws IOException {
    final long compressedSize = archive.length();
    final ProcessBuilder processBuilder = new ProcessBuilder(
            "7z", "x", archive.getAbsolutePath(), "-o" + extractionDir, "-aot"
    );
    try {
      execute(processBuilder);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      e.printStackTrace();
    }
    final long extractedSize = extractionDir.length();
    return ((double) compressedSize - extractedSize) / MEGABYTE;
  }

  private void execute(final ProcessBuilder processBuilder) throws IOException, InterruptedException {
    final Process process = processBuilder.start();
    process.waitFor();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      reader.lines().forEach(LOGGER::info);
    }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
      reader.lines().forEach(LOGGER::info);
    }
  }
}
