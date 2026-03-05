package com.sts.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaTopicsConfig.class)
public class KafkaTopicsConfig {
}
