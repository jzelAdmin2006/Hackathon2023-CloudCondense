package tech.bison.trainee.server.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
  public static String ensureNoTrailingSlash(String string) {
    return string.endsWith("/") ? string.substring(0, string.length() - 1) : string;
  }

  public static String ensureTrailingSlash(String string) {
    return string.endsWith("/") ? string : string + "/";
  }

  public static String ensureNoLeadingSlash(String string) {
    return string.startsWith("/") ? string.substring(1) : string;
  }

  @NotNull
  public static String ensureNoDavPrefix(String path) {
    return path.startsWith("/dav") ? path.substring(4) : path;
  }
}
