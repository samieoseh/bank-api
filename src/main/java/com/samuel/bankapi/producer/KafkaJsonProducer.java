package com.samuel.bankapi.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.samuel.bankapi.payload.TransactionMessage;
import com.samuel.bankapi.payload.UserMessage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaJsonProducer {
    private final KafkaTemplate<String, UserMessage> kafkaTemplate;
    private final KafkaTemplate<String, TransactionMessage> kafkaTransactionTemplate;

    public void sendMessage(UserMessage userMessage) {
        Message<UserMessage> message = MessageBuilder
                .withPayload(userMessage)
                .setHeader(KafkaHeaders.TOPIC, "UserTopic")
                .build();
        System.out.println("Producing message: " + userMessage);
        kafkaTemplate.send(message);
    }

    public void sendMessage(TransactionMessage transactionMessage) {
        Message<TransactionMessage> message = MessageBuilder
                .withPayload(transactionMessage)
                .setHeader(KafkaHeaders.TOPIC, "TransactionTopic")
                .build();
        System.out.println("Producing message: " + transactionMessage);
        kafkaTransactionTemplate.send(message);
    }
}
