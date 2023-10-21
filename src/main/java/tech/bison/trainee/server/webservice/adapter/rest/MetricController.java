package tech.bison.trainee.server.webservice.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.bison.trainee.server.business.service.MetricService;
import tech.bison.trainee.server.webservice.adapter.model.metric.MetricDto;
import tech.bison.trainee.server.webservice.service.WebMapperService;

@RestController
@RequestMapping("/metric")
public class MetricController {
    @Autowired
    private MetricService service;
    @Autowired
    private WebMapperService webMapperService;

    @GetMapping
    ResponseEntity<MetricDto> get() {
        return ResponseEntity.ok(webMapperService.toDto(service.get()));
    }
}
