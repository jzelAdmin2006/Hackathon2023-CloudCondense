package tech.bison.trainee.server.business.service.domain.condense.impl;

import static java.util.stream.Collectors.toList;
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
import tech.bison.trainee.server.business.domain.cloud_storage.CloudStorage;
import tech.bison.trainee.server.business.service.domain.condense.CondenseResource;
import tech.bison.trainee.server.business.service.domain.condense.CondenseStorage;
import tech.bison.trainee.server.common.davresource.ResourceURL;

public class DavStorageCondense implements CondenseStorage {

  public static final String WEBDAV_SERVER_URL = "http://localhost:80"; // make this dynamic

  private final Sardine sardine;

  public DavStorageCondense(CloudStorage storage) {
    this.sardine = SardineFactory.begin(storage.username(), storage.password());
  }

  @Override
  public List<CondenseResource> recursivelyList() {
    try {
      return recursivelyListDav().stream()
          .map(resource -> new DavResourceCondense(resource, sardine))
          .collect(toList());
    } catch (IOException e) {
      e.printStackTrace();
      return List.of();
    }
  }

  private List<DavResource> recursivelyListDav() throws IOException {
    final List<DavResource> resources = new ArrayList<>(sardine.list(WEBDAV_SERVER_URL).stream().skip(1).toList());
    final LinkedList<DavResource> queue = new LinkedList<>(resources);
    while (!queue.isEmpty()) {
      final DavResource resource = queue.poll();
      if (resource.isDirectory()) {
        final List<DavResource> childResources = sardine
            .list(ensureTrailingSlash(new ResourceURL(WEBDAV_SERVER_URL, resource).toString()))
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
      sardine.put(HttpUrl.parse(WEBDAV_SERVER_URL).resolve(location + file.getName()).toString(), is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
