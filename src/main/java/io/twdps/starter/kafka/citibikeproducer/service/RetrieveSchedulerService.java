package io.twdps.starter.kafka.citibikeproducer.service;

import io.twdps.starter.kafka.domain.Feed;
import io.twdps.starter.kafka.citibikeproducer.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

@Component
public class RetrieveSchedulerService {

  public static Logger logger = LoggerFactory.getLogger(RetrieveSchedulerService.class);

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private KafkaProducer kafkaProducer;

  @Scheduled(initialDelay = 2000, fixedRate = 10000)
  public void ScheduledProducerFlux() {
    logger.info("INSIDE SCHEDULED TASK With Flux");
    Feed feed = restTemplate.getForObject("https://gbfs.citibikenyc.com/gbfs/fr/station_status.json", Feed.class);
    if (feed.getData() != null) {
      logger.info("got station status master with {} status messages", feed.getData().getStations().size());
      kafkaProducer.accept(Flux.fromIterable(feed.getData().getStations()));
    } else {
      logger.error("got no data?");
    }
  }
}
