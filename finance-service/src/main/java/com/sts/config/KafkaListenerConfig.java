package com.sts.config;

import com.sts.event.InvoiceEvent;
import com.sts.event.PurchaseCreatedEvent;
import com.sts.topics.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    // ---------------- CONSUMER FACTORIES ----------------

    @Bean
    public ConsumerFactory<String, PurchaseCreatedEvent> purchaseConsumerFactory() {

        Map<String, Object> props = sharedConfig();

        JacksonJsonDeserializer<PurchaseCreatedEvent> deserializer =
                new JacksonJsonDeserializer<>(PurchaseCreatedEvent.class, false);

        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(deserializer)
        );
    }

    @Bean
    public ConsumerFactory<String, InvoiceEvent> invoiceConsumerFactory() {

        Map<String, Object> props = sharedConfig();

        JacksonJsonDeserializer<InvoiceEvent> deserializer =
                new JacksonJsonDeserializer<>(InvoiceEvent.class, false);

        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(deserializer)
        );
    }

    // ---------------- LISTENER FACTORIES ----------------

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PurchaseCreatedEvent>
    purchaseKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, PurchaseCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(purchaseConsumerFactory());

        configureFactory(factory);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InvoiceEvent>
    invoiceKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, InvoiceEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(invoiceConsumerFactory());

        configureFactory(factory);

        return factory;
    }


    // ---------------- SHARED CONFIG ----------------


    private <T> void configureFactory(ConcurrentKafkaListenerContainerFactory<String, T> factory) {

        factory.setConcurrency(1);

        factory.getContainerProperties().setAckMode(
                ContainerProperties.AckMode.MANUAL
        );

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));

        factory.setCommonErrorHandler(errorHandler);
    }

    public Map<String, Object> sharedConfig() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroup("finance-group"));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return props;
    }


}
