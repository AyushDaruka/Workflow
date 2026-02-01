package com.AD.Workflow.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic newNotificationTopic() {
        return TopicBuilder.name("notification-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
