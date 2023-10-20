package tech.bison.trainee.server.persistence.domain.cloud_storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudStoragePersistence extends JpaRepository<CloudStorageEntity, Integer> {
}
