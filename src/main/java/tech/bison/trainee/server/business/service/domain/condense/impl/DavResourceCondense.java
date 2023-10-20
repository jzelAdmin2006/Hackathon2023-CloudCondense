package tech.bison.trainee.server.business.service.domain.condense.impl;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static tech.bison.trainee.server.business.service.domain.condense.impl.DavStorageCondense.WEBDAV_SERVER_URL;
import static tech.bison.trainee.server.util.StringUtils.ensureNoTrailingSlash;
import static tech.bison.trainee.server.util.StringUtils.ensureTrailingSlash;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;

import lombok.AllArgsConstructor;
import tech.bison.trainee.server.business.service.domain.condense.CondenseResource;
import tech.bison.trainee.server.common.davresource.ResourceURL;

@AllArgsConstructor
public class DavResourceCondense implements CondenseResource {
  private final DavResource davResource;
  private final Sardine sardine;

  @Override
  public boolean isDirectory() {
    return davResource.isDirectory();
  }

  @Override
  public boolean isFile() {
    return !isDirectory();
  }

  @Override
  public String getName() {
    return davResource.getName();
  }

  @Override
  public String getFileContent() {
    try {
      return IOUtils.toString(
          sardine.get(ensureNoTrailingSlash(new ResourceURL(WEBDAV_SERVER_URL, davResource).toString())),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      return "";
    }
  }

  @Override
  public Date getModified() {
    return davResource.getModified();
  }

  @Override
  public void copyTo(File target) {
    try {
      target.getParentFile().mkdirs();
      if (isFile()) {
        copyResourceToFile(davResource, target);
      } else {
        final LinkedList<DavResource> queue = new LinkedList<>();
        queue.add(davResource);
        while (!queue.isEmpty()) {
          final DavResource currentChildResource = queue.poll();
          if (currentChildResource.isDirectory()) {
            childTarget(target, currentChildResource).mkdirs();
            queue.addAll(
                sardine.list(ensureNoTrailingSlash(new ResourceURL(WEBDAV_SERVER_URL, currentChildResource).toString()))
                    .stream()
                    .skip(1)
                    .toList());
          } else {
            copyResourceToFile(currentChildResource, childTarget(target, currentChildResource));
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getLocation() {
    return davResource.getPath().substring(0, davResource.getPath().length() - davResource.getName().length() - 1);
  }

  private File childTarget(final File parentTarget, DavResource childResource) {
    final String relativePath = childResource.getHref().getPath().substring(davResource.getHref().getPath().length());
    return new File(parentTarget, relativePath);
  }

  private void copyResourceToFile(DavResource resource, final File target) throws IOException {
    try (InputStream is = sardine.get(ensureNoTrailingSlash(new ResourceURL(WEBDAV_SERVER_URL, resource).toString()))) {
      copyInputStreamToFile(is, target);
    }
  }

  @Override
  public void delete() {
    try {
      final String resourceURL = new ResourceURL(WEBDAV_SERVER_URL, davResource).toString();
      sardine.delete(isDirectory() ? ensureTrailingSlash(resourceURL) : ensureNoTrailingSlash(resourceURL));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getPath() {
    return davResource.getPath();
  }
}
