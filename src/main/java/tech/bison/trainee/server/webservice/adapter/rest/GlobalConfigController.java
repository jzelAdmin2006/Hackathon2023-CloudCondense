package tech.bison.trainee.server.webservice.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bison.trainee.server.business.domain.global_config.GlobalConfig;
import tech.bison.trainee.server.business.service.CondenseService;
import tech.bison.trainee.server.business.service.GlobalConfigService;
import tech.bison.trainee.server.webservice.adapter.model.global_config.GlobalConfigDto;
import tech.bison.trainee.server.webservice.service.WebMapperService;

@RestController
@RequestMapping("/global-config")
public class GlobalConfigController {
    @Autowired
    private GlobalConfigService service;

    @Autowired
    private WebMapperService webMapperService;

    @Autowired
    private CondenseService condenseService;

    @GetMapping
    ResponseEntity<GlobalConfigDto> get() {
        return ResponseEntity.ok(webMapperService.toDto(service.get()));
    }

    @PutMapping
    ResponseEntity<GlobalConfig> update(GlobalConfigDto globalConfigDto) {
        ResponseEntity<GlobalConfig> response = ResponseEntity.ok(
                service.update(webMapperService.fromDto(globalConfigDto))
        );
        condenseService.updateScheduler();
        return response;
    }
}
