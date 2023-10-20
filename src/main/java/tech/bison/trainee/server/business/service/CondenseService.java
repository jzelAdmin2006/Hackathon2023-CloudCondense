package tech.bison.trainee.server.business.service;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static tech.bison.trainee.server.common.sevenzip.SevenZip.SEVEN_ZIP_FILE_ENDING;
import static tech.bison.trainee.server.util.StringUtils.ensureTrailingSlash;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tech.bison.trainee.server.business.domain.cloud_storage.CloudStorage;
import tech.bison.trainee.server.business.service.domain.condense.CondenseFactory;
import tech.bison.trainee.server.business.service.domain.condense.CondenseResource;
import tech.bison.trainee.server.business.service.domain.condense.CondenseStorage;
import tech.bison.trainee.server.common.sevenzip.SevenZip;
import tech.bison.trainee.server.config.ArchiveConfig;

@Service
@AllArgsConstructor
public class CondenseService {
  private static final int CONDENSE_AGE_MS = /* 24 * 60 * 60 * 1000 */ 0; // TODO make this dynamic
  private static final String CONDENSE_IGNORING_FILE_NAME = ".condenseignore";
  private static final String CONDENSE_GROUPING_FILE_NAME = ".condensegroup";
  private final ArchiveConfig archiveConfig;
  private final CloudStorageService storageService;
  private final CondenseFactory condenseFactory;
  private final ExecutorService archiving;

  @Scheduled(fixedRate = 3600000)
  public void condenseClouds() {
    storageService.getAllCloudStorageEntries().forEach(this::condense);
  }

  private void condense(CloudStorage storage) {
    newSingleThreadExecutor().submit(() -> condense(condenseFactory.translate(storage)));
  }

  private void condense(CondenseStorage storage) {
    final List<CondenseResource> recursiveResources = filterGrouping(filterIgnoring(storage.recursivelyList()));
    recursiveResources.forEach(resource -> archiving.submit(() -> condense(resource, storage)));
  }

  // TODO implement similar to gitignore
  private List<CondenseResource> filterIgnoring(List<CondenseResource> recursiveResources) {
    return recursiveResources.stream()
        .filter(resource -> !resource.getName().equals(CONDENSE_IGNORING_FILE_NAME))
        .toList();
  }

  // TODO implement similar to gitignore
  private List<CondenseResource> filterGrouping(List<CondenseResource> recursiveResources) {
    return recursiveResources.stream()
        .filter(resource -> !resource.getName().equals(CONDENSE_GROUPING_FILE_NAME)
            && recursiveResources.stream().noneMatch(otherResource -> otherResourceIsParent(resource, otherResource)))
        .toList();
  }

  private boolean otherResourceIsParent(CondenseResource resource, CondenseResource otherResource) {
    return ensureTrailingSlash(resource.getLocation()).contains(otherResource.getPath());
  }

  private void condense(CondenseResource resource, CondenseStorage storage) {
    if (resource.getModified().before(new Date(new Date().getTime() - CONDENSE_AGE_MS))
        && (resource.isDirectory() || !resource.getName().endsWith(SEVEN_ZIP_FILE_ENDING))) {
      try {
        archive(resource, storage);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // TODO implement that already archived files in a folder resource get extracted before
  // rearchiving them
  private void archive(CondenseResource resource, CondenseStorage storage) throws IOException {
    final File tmpWorkDir = new File(archiveConfig.getTmpWorkDir());
    final File target = new File(tmpWorkDir, resource.getName());
    try {
      resource.copyTo(target);
      final File archive = new File(archiveConfig.getTmpWorkDir(), target.getName() + SEVEN_ZIP_FILE_ENDING);
      new SevenZip().compress(target, archive);
      replaceResource(resource, archive, storage);
    } finally {
      cleanDirectory(tmpWorkDir);
    }
  }

  private void replaceResource(CondenseResource resource, File archive, CondenseStorage storage) {
    storage.upload(archive, ensureTrailingSlash(resource.getLocation()));
    resource.delete();
  }
}
