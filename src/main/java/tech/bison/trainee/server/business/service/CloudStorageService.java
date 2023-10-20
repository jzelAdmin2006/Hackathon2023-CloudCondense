package tech.bison.trainee.server.business.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.bison.trainee.server.business.domain.cloud_storage.CloudStorage;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageRepository;

@Service
public class CloudStorageService {

  @Autowired
  private CloudStorageRepository repository;

  public List<CloudStorage> getAllCloudStorageEntries() {
    return repository.findAll();
  }

  public CloudStorage addCloudStorageEntry(CloudStorage entry) {
    return repository.save(entry);
  }

  public Optional<CloudStorage> findById(int id) {
    return repository.findById(id);
  }

  public void delete(CloudStorage cloudStorage) {
    repository.delete(cloudStorage);
  }
}
