package com.samples.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    private static final String TOPIC = "topic-1";
    private static final String GROUP = "group-1";

    @KafkaListener(topics = TOPIC, groupId = GROUP)
    public void consumer(String message) {
        long current = System.currentTimeMillis();
        long data = Long.parseLong(message);
        LOGGER.info("received message is {}, diff: {}msec", message, current - data);
    }
}
