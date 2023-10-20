package tech.bison.trainee.server.persistence.domain.cloud_storage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CloudStorageType {
  GOOGLE_DRIVE("Google Drive", false),
  WEBDAV("WebDAV", true),
  UNKNOWN("Unknown", false);

  private final String displayName;
  private final boolean urlRequired;
}
