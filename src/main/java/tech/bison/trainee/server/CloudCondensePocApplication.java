package tech.bison.trainee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CloudCondenseApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudCondenseApplication.class, args);
  }

}
