package tech.bison.trainee.server.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.bison.trainee.server.business.domain.global_config.GlobalConfig;
import tech.bison.trainee.server.persistence.domain.global_config.GlobalConfigRepository;

@Service
public class GlobalConfigService {
    @Autowired
    private GlobalConfigRepository repository;

    public GlobalConfig get() {
        return repository.get();
    }

    public GlobalConfig update(GlobalConfig globalConfig) {
        return repository.update(globalConfig);
    }
}
