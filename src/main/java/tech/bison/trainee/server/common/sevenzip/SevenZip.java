package tech.bison.trainee.server.common.sevenzip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.bison.trainee.server.business.domain.Metric;
import tech.bison.trainee.server.business.service.MetricService;

import static java.lang.Thread.currentThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.logging.Logger;

public class SevenZip {
  private static final Logger LOGGER = Logger.getLogger(SevenZip.class.getName());

  public static final String SEVEN_ZIP_FILE_ENDING = ".7z";


  public double compress(File input, File archive) throws IOException {
    long originalSize = input.length();
    final ProcessBuilder processBuilder = new ProcessBuilder("7z", "a", "-t7z", "-mx=9", archive.getAbsolutePath(),
        input.getAbsolutePath());
    try {
      execute(processBuilder);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      throw new IOException("7z compression process was interrupted", e);
    }
    long compressedSize = archive.length();
    return ((double) (originalSize - compressedSize) / 1_000_000);
  }

  public double extractTo(File archive, File extractionDir) throws IOException {
    long compressedSize = archive.length();
    final ProcessBuilder processBuilder = new ProcessBuilder(
            "7z", "x", archive.getAbsolutePath(), "-o" + extractionDir, "-aot"
    );
    try {
      execute(processBuilder);
    } catch (InterruptedException e) {
      currentThread().interrupt();
      e.printStackTrace();
    }
    long extractedSize = extractionDir.length();
    return ((double) compressedSize - extractedSize) / 1_000_000;
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
