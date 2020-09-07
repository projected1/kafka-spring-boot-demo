package com.example.kafkademo.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(
        groupId = "group1",
        topicPartitions = @TopicPartition(topic = "example", partitions = "0"),
        containerFactory = "concurrentKafkaListenerContainerFactory1")
    public void consumerA(String message) {
        System.out.println("T:" + Thread.currentThread().getId() + " Group:1, Partitions:0     Message:" + message);
    }

    @KafkaListener(
        groupId = "group1",
        topicPartitions = @TopicPartition(topic = "example", partitions = { "1", "2" }),
        containerFactory = "concurrentKafkaListenerContainerFactory1")
    public void consumerB(String message) {
        System.out.println("T:" + Thread.currentThread().getId() + " Group:1, Partitions:1,2   Message:" + message);
    }

    @KafkaListener(
        groupId = "group2",
        topics = "example",
        containerFactory = "concurrentKafkaListenerContainerFactory2")
    public void consumerC(String message) {
        System.out.println("T:" + Thread.currentThread().getId() + " Group:2, Partitions:0,1,2 Message:" + message);
    }

}
