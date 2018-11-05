package io.twdps.starter.kafka.citibikeproducer.kafka;

import io.twdps.starter.kafka.domain.StationStatus;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class KafkaProducerTests {

  public static Logger logger = LoggerFactory.getLogger(KafkaProducerTests.class);

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "citibike2");

  private String brokerURL;

  @Autowired
  private SenderOptions<String, StationStatus> senderOptions;

  @Autowired
  private ReceiverOptions<String, StationStatus> receiverOptions;

  @Autowired
  private Map<String, Object> producerConfigs;

  @Autowired
  private Map<String, Object> consumerConfigs;

  private KafkaSender<String, StationStatus> kafkaSender;

  private KafkaReceiver<String, StationStatus> kafkaReceiver;

  private KafkaProducer kafkaProducer;

  private String writeTopic = "citibike2";

  @Before
  public void setup() {

    brokerURL = embeddedKafka.getBrokersAsString();
    logger.info("BROKER URL:{}", brokerURL);
    SenderOptions<String, StationStatus> updatedSenderOptions = senderOptions.producerProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerURL);
    kafkaSender = KafkaSender.create(updatedSenderOptions);
    //update the brokerURL for the embedded test and add the topic as well
    ReceiverOptions updatedReceiverOptions = receiverOptions
        .consumerProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerURL)
        .consumerProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        .subscription(Collections.singleton("citibike2"));

    kafkaReceiver = KafkaReceiver.create(updatedReceiverOptions);

    kafkaProducer = new KafkaProducer();
    kafkaProducer.setKafkaSender(kafkaSender);
    kafkaProducer.setWriteTopic("citibike2");
  }

  @Test
  public void contextTests() {

    assertNotNull(embeddedKafka, "embedded cluster is null");
    logger.info("brokerURL is:{}", brokerURL);
    assertNotNull(kafkaSender, "kafka sender is null");
    assertNotNull(kafkaReceiver, "kafka receiver is null");
  }

  @Test
  public void sendOneTest() throws InterruptedException {

    StationStatus stationStatus = new StationStatus();
    stationStatus.setStationId("Station 1 Test");

    Flux<StationStatus> sendFlux = Flux.just(stationStatus);
    kafkaProducer.accept(sendFlux);
    Thread.sleep(500);

    ReceiverRecord<String, StationStatus> rr = kafkaReceiver.receive().blockFirst();
    assertNotNull(rr, "receiver record is null");
    StationStatus receivedStatus = rr.value();
    assertNotNull(receivedStatus, "receivedStatus is null");
    assertEquals(stationStatus.getStationId(), receivedStatus.getStationId());
  }

}
