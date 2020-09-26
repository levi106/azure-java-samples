package com.samples.kafka.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
    private static final String TOPIC = "topic-1";
    private static final String GROUP = "group-1";
    @Autowired
    protected MeterRegistry meterRegistry;
    private Counter receivedMessagesCounter;

    @PostConstruct
    public void init() {
        receivedMessagesCounter = meterRegistry.counter("api.kafka.received.messages");
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP)
    public void consumer(String message) {
        long current = System.currentTimeMillis();
        receivedMessagesCounter.increment();
        long data = Long.parseLong(message);
        LOGGER.info("received message is {}, diff: {}msec", message, current - data);
    }
}
