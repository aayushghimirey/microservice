package com.sts.infra.config;

import com.sts.topics.KafkaTopics;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaTopics.class)
public class KafkaTopicsConfig {
}
