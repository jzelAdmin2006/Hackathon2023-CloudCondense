package tech.bison.trainee.server.business.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.eclipse.jgit.ignore.IgnoreNode;
import org.eclipse.jgit.ignore.IgnoreNode.MatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.bison.trainee.server.business.domain.cloud_storage.CloudStorage;
import tech.bison.trainee.server.business.service.domain.condense.CondenseFactory;
import tech.bison.trainee.server.business.service.domain.condense.CondenseResource;
import tech.bison.trainee.server.business.service.domain.condense.CondenseStorage;
import tech.bison.trainee.server.common.sevenzip.SevenZip;
import tech.bison.trainee.server.config.ArchiveConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static tech.bison.trainee.server.common.sevenzip.SevenZip.SEVEN_ZIP_FILE_ENDING;
import static tech.bison.trainee.server.util.StringUtils.ensureTrailingSlash;

@Component
@Service
@AllArgsConstructor
public class CondenseService {
  private static final int CONDENSE_AGE_MS = /* 24 * 60 * 60 * 1000 */ 0; // TODO make this dynamic
  private static final String CONDENSE_IGNORING_FILE_NAME = ".condenseignore";
  private final ArchiveConfig archiveConfig;
  private final CloudStorageService storageService;
  private final CondenseFactory condenseFactory;
  private final ExecutorService archiving;
  @Autowired
  private final GlobalConfigService globalConfigService;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @PostConstruct
  public void initializeScheduler() {
    updateScheduler();
  }

  public void updateScheduler() {
    long rate = globalConfigService.get().scheduleRate();
    scheduler.scheduleAtFixedRate(this::condenseClouds, 0, rate, TimeUnit.MILLISECONDS);
  }

  public void condenseClouds() {
    storageService.getAllCloudStorageEntries().forEach(this::condense);
  }

  public void condense(CloudStorage storage) {
    try (ExecutorService condenseExecutor = newSingleThreadExecutor()) {
      condenseExecutor.submit(() -> condense(condenseFactory.translate(storage)));
    }
  }

  private void condense(CondenseStorage storage) {
    final List<CondenseResource> recursiveResources = filterIgnoring(storage.recursivelyList());
    recursiveResources.forEach(resource -> archiving.submit(() -> condense(resource, storage, recursiveResources)));
  }

  private List<CondenseResource> filterIgnoring(List<CondenseResource> recursiveResources) {
    final Optional<CondenseResource> ignoringCriteria = recursiveResources.stream()
        .filter(this::isCondenseIgnoreFileItself)
        .findFirst();
    if (ignoringCriteria.isPresent()) {
      final String ignorePatterns = ignoringCriteria.get().getFileContent();
      return recursiveResources.stream()
          .filter(resource -> !isCondenseIgnoreFileItself(resource))
          .filter(resource -> !isIgnored(resource.getPath(), ignorePatterns))
          .toList();
    } else {
      return recursiveResources.stream().filter(resource -> !isCondenseIgnoreFileItself(resource)).toList();
    }
  }

  private boolean isCondenseIgnoreFileItself(CondenseResource resource) {
    return resource.getName().equals(CONDENSE_IGNORING_FILE_NAME) && resource.isInRoot();
  }

  private boolean isIgnored(String path, String gitignoreContent) {
    final IgnoreNode ignoreNode = new IgnoreNode();
    try (InputStream stream = new ByteArrayInputStream(gitignoreContent.getBytes(StandardCharsets.UTF_8))) {
      ignoreNode.parse(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ignoreNode.isIgnored(path, true) == MatchResult.IGNORED;
  }

  private boolean otherResourceIsParent(CondenseResource resource, CondenseResource otherResource) {
    return ensureTrailingSlash(resource.getLocation()).contains(otherResource.getPath());
  }

  private void condense(CondenseResource resource, CondenseStorage storage, List<CondenseResource> recursiveResources) {
    if (shouldBeArchived(resource) && recursiveResources.stream()
        .noneMatch(
            otherResource -> otherResourceIsParent(resource, otherResource) && shouldBeArchived(otherResource))) {
      try {
        archive(resource, storage);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean shouldBeArchived(CondenseResource resource) {
    return resource.getModified().before(new Date(new Date().getTime() - CONDENSE_AGE_MS))
        && (resource.isDirectory() || !resource.getName().endsWith(SEVEN_ZIP_FILE_ENDING));
  }

  private void archive(CondenseResource resource, CondenseStorage storage) throws IOException {
    final File tmpWorkDir = new File(archiveConfig.getTmpWorkDir());
    final File target = new File(tmpWorkDir, resource.getName());

    try {
      resource.copyTo(target);
      if (resource.isDirectory()) {
        extractExistingArchives(target);
      }
      final File archive = new File(archiveConfig.getTmpWorkDir(), target.getName() + SEVEN_ZIP_FILE_ENDING);
      new SevenZip().compress(target, archive);
      replaceResource(resource, archive, storage);
    } finally {
      cleanDirectory(tmpWorkDir);
    }
  }

  private void extractExistingArchives(File startDir) throws IOException {
    final Queue<File> directories = new LinkedList<>();
    directories.add(startDir);

    while (!directories.isEmpty()) {
      final File currentDir = directories.poll();
      final File[] files = currentDir.listFiles();
      for (File file : files) {
        if (file.isDirectory()) {
          directories.add(file);
        } else if (file.getName().endsWith(SEVEN_ZIP_FILE_ENDING)) {
          new SevenZip().extractTo(file,
              new File(file.getParentFile(), file.getName().replace(SEVEN_ZIP_FILE_ENDING, "")).getParentFile());
          Files.delete(file.toPath());
        }
      }
    }
  }

  private void replaceResource(CondenseResource resource, File archive, CondenseStorage storage) {
    storage.upload(archive, ensureTrailingSlash(resource.getLocation()));
    resource.delete();
  }
}
