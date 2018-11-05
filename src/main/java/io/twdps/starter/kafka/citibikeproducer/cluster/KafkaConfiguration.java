package io.twdps.starter.kafka.citibikeproducer.cluster;

import io.twdps.starter.kafka.domain.StationStatus;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfiguration {

  public static Logger logger = LoggerFactory.getLogger(KafkaConfiguration.class);

  @Value("${kafka.brokers}")
  private String kafkaBrokers;

  @Value("${write.topic}")
  private String writeTopic;

  @Bean
  public Map<String, Object> producerConfigs() {
    logger.info("THIS IS THE KAFKA BROKERS URL->{}", kafkaBrokers);
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "bikestream-producer");
    // See https://kafka.apache.org/documentation/#producerconfigs for more properties
    return props;
  }

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    // list of host:port pairs used for establishing the initial connections to the Kafka cluster
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafkaBrokers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
    );
    //key learning in newest Spring Kafka dependencies - must add the custom package to the trusted packages
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "io.twdps.starter.kafka.domain");
    // allows a pool of processes to divide the work of consuming and processing records
    // automatically reset the offset to the earliest offset
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    return props;
  }

  @Bean
  public SenderOptions<String, StationStatus> senderOptions() {
    return SenderOptions.create(producerConfigs());
  }

  @Bean
  public ReceiverOptions<String, StationStatus> receiverOptions() {
    return ReceiverOptions.create(consumerConfigs());
  }

  @Bean
  public KafkaSender<String, StationStatus> kafkaSender() {
    SenderOptions<String, StationStatus> senderOptions = SenderOptions.create(producerConfigs());
    return KafkaSender.create(senderOptions);

  }
}
