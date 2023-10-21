package tech.bison.trainee.server.persistence.domain.metric;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricPersistence extends JpaRepository<MetricEntity, Integer> {
}
