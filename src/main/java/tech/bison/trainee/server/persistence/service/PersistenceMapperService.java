package tech.bison.trainee.server.persistence.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.bison.trainee.server.business.domain.cloud_storage.CloudStorage;
import tech.bison.trainee.server.business.domain.global_config.GlobalConfig;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageEntity;
import tech.bison.trainee.server.persistence.domain.global_config.GlobalConfigEntity;

@Service
public class PersistenceMapperService {
  @Autowired
  private EncryptionService encryptionService;

  public List<CloudStorage> fromCloudStorageEntities(List<CloudStorageEntity> entities) {
    return entities.stream().map(this::fromEntity).toList();
  }

  public CloudStorage fromEntity(CloudStorageEntity entity) {
    return new CloudStorage(entity.getId(), entity.getName(), entity.getType(), Optional.ofNullable(entity.getUrl()),
        entity.getUsername(), encryptionService.decrypt(entity.getPassword()), entity.getCreated());
  }

  public CloudStorageEntity toEntity(CloudStorage entry) {
    return new CloudStorageEntity(entry.id(), entry.name(), entry.type(), entry.url().orElse(null), entry.username(),
        encryptionService.encrypt(entry.password()), entry.created());
  }

  public GlobalConfig fromEntity(GlobalConfigEntity entry) {
    return new GlobalConfig(entry.getId(), entry.getScheduleRate());
  }

  public GlobalConfigEntity toEntity(GlobalConfig entry) {
    return new GlobalConfigEntity(entry.id(), entry.scheduleRate());
  }
}
