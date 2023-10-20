package tech.bison.trainee.server.business.service.domain.condense.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import tech.bison.trainee.server.business.service.domain.condense.CondenseResource;
import tech.bison.trainee.server.business.service.domain.condense.CondenseStorage;

public class UnsupportedCondense implements CondenseResource, CondenseStorage {

  @Override
  public List<CondenseResource> recursivelyList() {
    return List.of();
  }

  @Override
  public boolean isFile() {
    return false;
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getFileContent() {
    return "";
  }

  @Override
  public Date getModified() {
    return new Date();
  }

  @Override
  public void copyTo(File target) {
    // unsupported
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public void upload(File file, String location) {
    // unsupported
  }

  @Override
  public String getLocation() {
    return "";
  }

  @Override
  public void delete() {
    // unsupported
  }

  @Override
  public String getPath() {
    return "";
  }

}
