package tech.bison.trainee.server.persistence.domain.cloud_storage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CloudStorageType {
  GOOGLE_DRIVE("Google Drive"),
  WEBDAV("WebDAV"),
  UNKNOWN("Unknown");

  private final String displayName;
}
