package tech.bison.trainee.server.business.service.domain.condense;

import java.io.File;
import java.util.Date;

public interface CondenseResource {
  boolean isFile();

  String getName();

  String getFileContent();

  Date getModified();

  void copyTo(File target);

  boolean isDirectory();

  String getLocation();

  void delete();

  String getPath();
}
