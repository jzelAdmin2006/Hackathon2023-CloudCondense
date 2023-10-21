package tech.bison.trainee.server.persistence.domain.cloud_storage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CloudStorageType {
  WEBDAV("WebDAV", true),
  GOOGLE_DRIVE("Google Drive", false),
  DROPBOX("Dropbox", false),
  ONEDRIVE("OneDrive", false),
  UNKNOWN("Unknown", false);

  private final String displayName;
  private final boolean urlRequired;
}
