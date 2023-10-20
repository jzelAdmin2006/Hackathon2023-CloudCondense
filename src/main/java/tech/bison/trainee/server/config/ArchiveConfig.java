package tech.bison.trainee.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "archive")
@Data
public class ArchiveConfig {
  private String tmpWorkDir;
}
