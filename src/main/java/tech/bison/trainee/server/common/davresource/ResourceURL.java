package tech.bison.trainee.server.common.davresource;

import static java.util.Objects.requireNonNull;

import com.github.sardine.DavResource;

import lombok.AllArgsConstructor;
import okhttp3.HttpUrl;

@AllArgsConstructor
public class ResourceURL {
  private final String baseUrl;
  private final DavResource resource;

  @Override
  public String toString() {
    return requireNonNull(HttpUrl.parse(baseUrl), "URL is invalid").resolve(resource.getHref().getPath()).toString();
  }
}
