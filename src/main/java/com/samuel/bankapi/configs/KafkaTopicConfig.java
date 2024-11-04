package com.samuel.bankapi.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic userTopic() {
        return TopicBuilder
                .name("UserTopic")
                .build();
    }

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder
                .name("TransactionTopic")
                .build();
    }
}
