package tech.bison.trainee.server.business.service.domain.condense;

import java.io.File;
import java.util.List;

public interface CondenseStorage {

  List<CondenseResource> recursivelyList();

  void upload(File file, String location);

}
