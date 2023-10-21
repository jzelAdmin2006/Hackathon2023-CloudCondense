package tech.bison.trainee.server.persistence.domain.metric;

import org.springframework.stereotype.Service;
import tech.bison.trainee.server.business.domain.Metric;
import tech.bison.trainee.server.persistence.service.PersistenceMapperService;

@Service
public class MetricRepository {
    private final MetricPersistence metricPersistence;
    private final PersistenceMapperService mapperService;

    public MetricRepository(MetricPersistence metricPersistence,
                                  PersistenceMapperService mapperService) {
        this.metricPersistence = metricPersistence;
        this.mapperService = mapperService;
    }

    public Metric get() {
        return mapperService.fromEntity(metricPersistence.findAll().getFirst());
    }

    public Metric update(Metric metric) {
        metricPersistence.deleteAll();
        return mapperService.fromEntity(metricPersistence.save(mapperService.toEntity(metric)));
    }
}
