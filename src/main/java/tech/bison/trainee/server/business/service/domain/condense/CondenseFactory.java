package tech.bison.trainee.server.business.service.domain.condense;

import org.springframework.stereotype.Service;

import tech.bison.trainee.server.business.domain.CloudStorage;
import tech.bison.trainee.server.business.service.domain.condense.impl.DavStorageCondense;
import tech.bison.trainee.server.business.service.domain.condense.impl.UnsupportedCondense;

@Service
public class CondenseFactory {
  public CondenseStorage translate(CloudStorage storage) {
    switch (storage.type()) {
      case WEBDAV:
        return new DavStorageCondense(storage);
      case GOOGLE_DRIVE, UNKNOWN, DROPBOX, ONEDRIVE:
        return new UnsupportedCondense();
    }
    throw new UnsupportedOperationException("Storage type %s implementation is undefined".formatted(storage.type()));
  }
}
