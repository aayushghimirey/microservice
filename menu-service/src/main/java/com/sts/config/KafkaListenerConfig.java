package com.sts.config;


import com.sts.event.StockEvent;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import javax.print.DocFlavor;
import java.util.Map;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {


    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, StockEvent> kafkaConsumer() {

        JacksonJsonDeserializer<StockEvent> deserializer = new JacksonJsonDeserializer<>(StockEvent.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(sharedConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockEvent> stockListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumer());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    public Map<String, Object> sharedConfig() {

        String bootstrapServers = kafkaProperties.getBootstrapServers();
        String groupId = kafkaProperties.getGroup("menu-service-group");

        Objects.requireNonNull(bootstrapServers, "kafka.bootstrap-servers is not configured");
        Objects.requireNonNull(groupId, "kafka group 'menu-service-group' is not configured");

        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId
        );
    }

}
