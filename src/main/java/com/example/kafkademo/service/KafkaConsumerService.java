package com.example.kafkademo.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(
        groupId = "group1",
        topicPartitions = @TopicPartition(topic = "example", partitions = "0"))
    public void consumerA(String message) {
        System.out.println("T:" + Thread.currentThread().getId() + " Partitions:0   Message:" + message);
    }

    @KafkaListener(
        groupId = "group1",
        topicPartitions = @TopicPartition(topic = "example", partitions = { "1", "2" }))
    public void consumerB(String message) {
        System.out.println("T:" + Thread.currentThread().getId() + " Partitions:1,2 Message:" + message);
    }

}
