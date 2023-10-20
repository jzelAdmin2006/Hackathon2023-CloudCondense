package tech.bison.trainee.server.persistence.domain.global_config;

import org.springframework.stereotype.Service;
import tech.bison.trainee.server.business.domain.global_config.GlobalConfig;
import tech.bison.trainee.server.persistence.service.PersistenceMapperService;

@Service
public class GlobalConfigRepository {
    private final GlobalConfigPersistence globalConfigPersistence;
    private final PersistenceMapperService mapperService;

    public GlobalConfigRepository(GlobalConfigPersistence globalConfigPersistence,
                                  PersistenceMapperService mapperService) {
        this.globalConfigPersistence = globalConfigPersistence;
        this.mapperService = mapperService;
    }

    public GlobalConfig get() {
        return mapperService.fromEntity(globalConfigPersistence.findAll().getFirst());
    }

    public GlobalConfig update(GlobalConfig globalConfig) {
        globalConfigPersistence.deleteAll();
        return mapperService.fromEntity(globalConfigPersistence.save(mapperService.toEntity(globalConfig)));
    }
}
