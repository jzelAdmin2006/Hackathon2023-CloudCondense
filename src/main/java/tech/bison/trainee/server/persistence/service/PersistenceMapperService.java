package tech.bison.trainee.server.persistence.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.bison.trainee.server.business.domain.CloudStorage;
import tech.bison.trainee.server.business.domain.GlobalConfig;
import tech.bison.trainee.server.business.domain.Metric;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageEntity;
import tech.bison.trainee.server.persistence.domain.global_config.GlobalConfigEntity;
import tech.bison.trainee.server.persistence.domain.metric.MetricEntity;

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

  public GlobalConfig fromEntity(GlobalConfigEntity entity) {
    return new GlobalConfig(entity.getId(), entity.getScheduleRate(), entity.getCondenseAge());
  }

  public GlobalConfigEntity toEntity(GlobalConfig entry) {
    return new GlobalConfigEntity(entry.id(), entry.scheduleRate(), entry.condenseAge());
  }

  public Metric fromEntity(MetricEntity entity) {
    return new Metric(entity.getId(), entity.getSavedDiskSpace());
  }

  public MetricEntity toEntity(Metric entry) {
    return new MetricEntity(entry.id(), entry.savedDiskSpace());
  }
}
