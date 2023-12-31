package tech.bison.trainee.server.webservice.adapter.rest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.bison.trainee.server.business.domain.CloudStorage;
import tech.bison.trainee.server.business.service.CloudStorageService;
import tech.bison.trainee.server.business.service.CondenseService;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageType;
import tech.bison.trainee.server.webservice.adapter.model.cloud_storage.CloudStorageRequestDto;
import tech.bison.trainee.server.webservice.adapter.model.cloud_storage.CloudStorageResourceDto;
import tech.bison.trainee.server.webservice.adapter.model.cloud_storage.CloudStorageTypeResourceDto;
import tech.bison.trainee.server.webservice.service.WebMapperService;

@RestController
@RequestMapping("/cloud-storage")
public class CloudStorageController {
  @Autowired
  private CloudStorageService service;

  @Autowired
  private WebMapperService webMapperService;

  @Autowired
  private CondenseService condenseService;

  @GetMapping
  public ResponseEntity<List<CloudStorageResourceDto>> getAllEntries() {
    return ResponseEntity.ok(service.getAllCloudStorageEntries()
        .stream()
        .sorted(Comparator.comparing(CloudStorage::created).reversed())
        .map(webMapperService::toDto)
        .toList());
  }

  @PostMapping
  public ResponseEntity<CloudStorageResourceDto> addEntry(@RequestBody CloudStorageRequestDto entry) {
    return ResponseEntity.ok(webMapperService.toDto(service.addCloudStorageEntry(webMapperService.fromDto(entry))));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CloudStorageResourceDto> deleteEntry(@PathVariable int id) {
    return service.findById(id).map(entry -> {
      service.delete(entry);
      return ResponseEntity.ok(webMapperService.toDto(entry));
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/types")
  public ResponseEntity<List<CloudStorageTypeResourceDto>> getCloudStorageTypes() {
    return ResponseEntity.ok(Arrays.stream(CloudStorageType.values())
        .filter(type -> type != CloudStorageType.UNKNOWN)
        .map(webMapperService::toDto)
        .toList());
  }

  @PostMapping("/{id}/condense")
  public ResponseEntity<Map<String, String>> condenseCloud(@PathVariable int id) {
    return service.findById(id).map(entry -> {
      condenseService.condense(entry);
      return ResponseEntity.ok(Map.of("message", "Condense for cloud with id %d queued".formatted(id)));
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
