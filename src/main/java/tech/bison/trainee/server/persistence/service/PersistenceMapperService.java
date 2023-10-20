package tech.bison.trainee.server.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.bison.trainee.server.business.domain.cloud_storage.CloudStorage;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageEntity;

@Service
public class PersistenceMapperService {
  @Autowired
  private EncryptionService encryptionService;

  public List<CloudStorage> fromCloudStorageEntities(List<CloudStorageEntity> entities) {
    return entities.stream().map(this::fromEntity).toList();
  }

  public CloudStorage fromEntity(CloudStorageEntity entity) {
    return new CloudStorage(entity.getId(), entity.getName(), entity.getType(), entity.getUsername(),
        encryptionService.decrypt(entity.getPassword()), entity.getCreated());
  }

  public CloudStorageEntity toEntity(CloudStorage entry) {
    return new CloudStorageEntity(entry.id(), entry.name(), entry.type(), entry.username(),
        encryptionService.encrypt(entry.password()), entry.created());
  }

}
