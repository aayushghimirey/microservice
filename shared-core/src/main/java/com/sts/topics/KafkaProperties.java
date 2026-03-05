package com.sts.topics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private Map<String, String> topics;
    private Map<String, String> groups;

    public String getTopic(String key) {
        return topics.get(key);
    }

    public String getGroup(String key) {
        return groups.get(key);
    }
}