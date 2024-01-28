package tech.bison.trainee.server.common.davresource;

import static java.util.Objects.requireNonNull;
import static tech.bison.trainee.server.util.StringUtils.ensureNoDavPrefix;
import static tech.bison.trainee.server.util.StringUtils.ensureNoLeadingSlash;

import com.github.sardine.DavResource;

import lombok.AllArgsConstructor;
import okhttp3.HttpUrl;
import tech.bison.trainee.server.util.StringUtils;

@AllArgsConstructor
public class ResourceURL {
  private final String baseUrl;
  private final DavResource resource;

  @Override
  public String toString() {
    return requireNonNull(HttpUrl.parse(baseUrl), "URL is invalid")
        .resolve(ensureNoLeadingSlash(ensureNoDavPrefix(resource.getHref().getPath()))).toString();
  }
}
