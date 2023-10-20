package tech.bison.trainee.server.common.sevenzip;

import static java.lang.Thread.currentThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class SevenZip {
  private static final Logger LOGGER = Logger.getLogger(SevenZip.class.getName());

  public static final String SEVEN_ZIP_FILE_ENDING = ".7z";

  public void compress(File input, File archive) throws IOException {
    final ProcessBuilder processBuilder = new ProcessBuilder("7z", "a", "-t7z", "-mx=9", archive.getAbsolutePath(),
        input.getAbsolutePath());
    try {
      execute(processBuilder);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw new IOException("7z compression process was interrupted", e);
    }
  }

  public void extractTo(File archive, File extractionDir) throws IOException {
    final ProcessBuilder processBuilder = new ProcessBuilder("7z", "x", archive.getAbsolutePath(), "-o" + extractionDir,
        "-aot");
    try {
      execute(processBuilder);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      e.printStackTrace();
    }
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
