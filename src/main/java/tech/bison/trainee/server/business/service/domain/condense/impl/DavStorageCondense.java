package tech.bison.trainee.server.business.service.domain.condense.impl;

import static java.util.stream.Collectors.toList;
import static tech.bison.trainee.server.util.StringUtils.ensureNoLeadingSlash;
import static tech.bison.trainee.server.util.StringUtils.ensureTrailingSlash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import okhttp3.HttpUrl;
import tech.bison.trainee.server.business.domain.CloudStorage;
import tech.bison.trainee.server.business.service.domain.condense.CondenseResource;
import tech.bison.trainee.server.business.service.domain.condense.CondenseStorage;
import tech.bison.trainee.server.common.davresource.ResourceURL;

public class DavStorageCondense implements CondenseStorage {

  public final String webdavServerUrl;

  private final Sardine sardine;

  public DavStorageCondense(CloudStorage storage) {
    this.sardine = SardineFactory.begin(storage.username(), storage.password());
    webdavServerUrl = storage.url().orElseThrow();
  }

  @Override
  public List<CondenseResource> recursivelyList() {
    try {
      return recursivelyListDav().stream()
          .map(resource -> new DavResourceCondense(resource, sardine, webdavServerUrl))
          .collect(toList());
    } catch (IOException e) {
      e.printStackTrace();
      return List.of();
    }
  }

  private List<DavResource> recursivelyListDav() throws IOException {
    final List<DavResource> resources = new ArrayList<>(sardine.list(webdavServerUrl).stream()
        .skip(1).filter(davResource -> !davResource.getName().equals(".zfs")).toList());
    final LinkedList<DavResource> queue = new LinkedList<>(resources);
    while (!queue.isEmpty()) {
      final DavResource resource = queue.poll();
      if (resource.isDirectory()) {
        final List<DavResource> childResources = sardine
            .list(ensureTrailingSlash(new ResourceURL(webdavServerUrl, resource).toString()))
            .stream()
            .skip(1)
            .toList();
        queue.addAll(childResources);
        resources.addAll(childResources);
      }
    }
    return resources;
  }

  @Override
  public void upload(File file, String location) {
    try (InputStream is = new FileInputStream(file)) {
      final List<String> list = sardine.list(HttpUrl.parse(webdavServerUrl)
              .resolve(ensureNoLeadingSlash(location)).toString())
          .stream()
          .map(DavResource::getName)
          .skip(1)
          .toList();
      String fileName = file.getName();

      int counter = 2;
      while (list.contains(fileName)) {
        final int dotIndex = fileName.lastIndexOf(".");
        fileName = file.getName();
        if (dotIndex != -1) {
          fileName = fileName.substring(0, dotIndex) + (counter++) + fileName.substring(dotIndex, fileName.length());
        } else {
          fileName += counter++;
        }
      }

      sardine.put(HttpUrl.parse(webdavServerUrl)
          .resolve(ensureNoLeadingSlash(location + fileName)).toString(), is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
