package com.sts.config;


import com.sts.event.StockEvent;
import com.sts.topics.KafkaProperties;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import javax.print.DocFlavor;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {


    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, StockEvent> kafkaConsumer() {
        return new DefaultKafkaConsumerFactory<>(sharedConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumer());
        return factory;
    }

    public Map<String, Object> sharedConfig() {
        JacksonJsonDeserializer<StockEvent> deserializer = new JacksonJsonDeserializer<>(StockEvent.class);
        deserializer.addTrustedPackages("*");
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroup("menu-service-group")
        );
    }

}
