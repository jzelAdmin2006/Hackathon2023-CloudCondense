package tech.bison.trainee.server.persistence.domain.global_config;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.bison.trainee.server.persistence.domain.cloud_storage.CloudStorageType;

import java.util.Date;

@Entity
@Table(name = "cloud_storage")
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GlobalConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int scheduleRate;
}
