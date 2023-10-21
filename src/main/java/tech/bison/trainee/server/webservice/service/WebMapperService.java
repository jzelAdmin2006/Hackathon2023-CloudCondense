package tech.bison.trainee.server.webservice.service;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import tech.bison.trainee.server.business.domain.CloudStorage;
import tech.bison.trainee.server.business.domain.GlobalConfig;
import tech.bison.trainee.server.business.domain.Metric;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageType;
import tech.bison.trainee.server.webservice.adapter.model.cloud_storage.CloudStorageRequestDto;
import tech.bison.trainee.server.webservice.adapter.model.cloud_storage.CloudStorageResourceDto;
import tech.bison.trainee.server.webservice.adapter.model.cloud_storage.CloudStorageTypeResourceDto;
import tech.bison.trainee.server.webservice.adapter.model.global_config.GlobalConfigDto;
import tech.bison.trainee.server.webservice.adapter.model.metric.MetricDto;

@Service
public class WebMapperService {

  private static final int INVALID_ID = 0;

  public CloudStorageResourceDto toDto(CloudStorage cloudStorage) {
    return new CloudStorageResourceDto(cloudStorage.id(), cloudStorage.name(), toDto(cloudStorage.type()),
        cloudStorage.username());
  }

  public CloudStorage fromDto(CloudStorageRequestDto cloudStorageDto) {
    return new CloudStorage(INVALID_ID, cloudStorageDto.name(),
        Stream.of(CloudStorageType.values())
            .filter(type -> type.getDisplayName().equals(cloudStorageDto.type()))
            .findFirst()
            .orElse(CloudStorageType.UNKNOWN),
        Optional.ofNullable(cloudStorageDto.url()), cloudStorageDto.username(), cloudStorageDto.password(), new Date());
  }

  public CloudStorageTypeResourceDto toDto(CloudStorageType type) {
    return new CloudStorageTypeResourceDto(type.getDisplayName(), type.isUrlRequired());
  }

  public GlobalConfigDto toDto(GlobalConfig globalConfig) {
    return new GlobalConfigDto(globalConfig.scheduleRate(), globalConfig.condenseAge());
  }

  public GlobalConfig fromDto(GlobalConfigDto globalConfigDto) {
    return new GlobalConfig(1, globalConfigDto.scheduleRate(), globalConfigDto.condenseAge());
  }

  public MetricDto toDto(Metric metric) {
    return new MetricDto(metric.id());
  }

  public Metric fromDto(MetricDto metricDto) {
    return new Metric(1, metricDto.savedDiskSpace());
  }
}
