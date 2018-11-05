package io.twdps.starter.kafka.citibikeproducer.kafka;

import io.twdps.starter.kafka.domain.StationStatus;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.function.Consumer;

@Service
public class KafkaProducer implements Consumer<Flux<StationStatus>> {

  public static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

  @Value("${write.topic}")
  private String writeTopic;

  @Autowired
  private KafkaSender<String, StationStatus> kafkaSender;

  @Override
  public void accept(Flux<StationStatus> stationStatusFlux) {
    kafkaSender.<Object>send(stationStatusFlux.map(st ->
        SenderRecord.create(new ProducerRecord<>(writeTopic, st.getStationId(), st), st.getStationId())))
        .doOnError(e -> logger.error("Send Failed", e))
        .subscribe(r -> {
          logger.debug("Success sending message for station id:{}", r.correlationMetadata());
        });
  }

  public void setWriteTopic(String writeTopic) {
    this.writeTopic = writeTopic;
  }

  public void setKafkaSender(KafkaSender<String, StationStatus> kafkaSender) {
    this.kafkaSender = kafkaSender;
  }
}
