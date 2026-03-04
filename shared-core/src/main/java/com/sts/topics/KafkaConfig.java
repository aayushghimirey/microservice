package com.sts.topics;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
public static class KafkaConfig {
    private String bootstrapServers;
    private Map<String, String> topics;

    public String getTopic(String key) {
        return topics != null ? topics.get(key) : null;
    }
}