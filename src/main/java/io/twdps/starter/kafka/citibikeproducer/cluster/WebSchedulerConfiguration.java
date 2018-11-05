package io.twdps.starter.kafka.citibikeproducer.cluster;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebSchedulerConfiguration {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public TaskScheduler taskScheduler() {
    return new ConcurrentTaskScheduler();
  }
}
