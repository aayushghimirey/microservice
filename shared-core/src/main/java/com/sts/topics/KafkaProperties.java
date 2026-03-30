package com.sts.topics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;


@Component
@ConfigurationProperties(prefix = "app.kafka")
@Validated
public class KafkaProperties {

    private String bootstrapServers;
    private Map<String, String> topics;
    private Map<String, String> groups;

    public String getBootstrapServers() {
        if (bootstrapServers == null) {
            throw new IllegalArgumentException(
                    "Bootstrap server not configured in application.yml"
            );
        }
        return bootstrapServers;
    }

    public String getTopic(String key) {

        String topic = topics.get(key);
        if (topic == null) {
            throw new IllegalArgumentException(
                    "Kafka topic '" + key + "' not configured in application.yml"
            );
        }
        return topic;
    }

    public String getGroup(String key) {
        String value = groups.get(key);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Kafka group '" + key + "' not configured in application.yml"
            );
        }
        return value;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }

    public void setGroups(Map<String, String> groups) {
        this.groups = groups;
    }

}