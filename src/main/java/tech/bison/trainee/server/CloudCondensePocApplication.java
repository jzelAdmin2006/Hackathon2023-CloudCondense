package tech.bison.trainee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CloudCondensePocApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudCondensePocApplication.class, args);
  }

}
