package tech.bison.trainee.server.persistence.domain.global_config;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigPersistence extends JpaRepository<GlobalConfigEntity, Integer> {
}
