package tech.bison.trainee.server.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.bison.trainee.server.business.domain.Metric;
import tech.bison.trainee.server.persistence.domain.metric.MetricRepository;

@Service
public class MetricService {
    @Autowired
    private MetricRepository repository;

    public Metric get() {
        return repository.get();
    }

    public Metric update(Metric metric) {
        return repository.update(metric);
    }
}
