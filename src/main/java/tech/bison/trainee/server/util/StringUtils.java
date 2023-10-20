package tech.bison.trainee.server.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
  public static String ensureNoTrailingSlash(String string) {
    return string.endsWith("/") ? string.substring(0, string.length() - 1) : string;
  }

  public static String ensureTrailingSlash(String string) {
    return string.endsWith("/") ? string : string + "/";
  }
}
