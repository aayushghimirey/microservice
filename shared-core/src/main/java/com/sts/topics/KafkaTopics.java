package com.sts.topics;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka.topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaTopics {
    private String purchaseEvent;
    private String orderEvent;
}