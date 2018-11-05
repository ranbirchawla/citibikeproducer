package io.twdps.starter.kafka.citibikeproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CitibikeproducerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CitibikeproducerApplication.class, args);
  }
}
