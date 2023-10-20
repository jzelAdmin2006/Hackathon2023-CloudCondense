package tech.bison.trainee.server.business.domain.cloud_storage;

import java.util.Date;
import java.util.Optional;

import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageType;

public record CloudStorage(int id, String name, CloudStorageType type, Optional<String> url, String username,
    String password, Date created) {
}
