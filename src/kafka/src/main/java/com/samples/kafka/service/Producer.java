package com.samples.kafka.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;

@Service
public class Producer implements ProducerListener<Object, Object>  {
    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "topic-1";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
        long current = System.currentTimeMillis();
        long sentTime = Long.parseLong(value.toString());
        LOG.info("topic: {}, partition: {}, key: {}, value: {}, diff: {}msec", topic, partition, key, value, current - sentTime);
    };

    public void sendMessage() {
        long current = System.currentTimeMillis();
        String message = Long.toString(current);
        kafkaTemplate.send(TOPIC, message);
    }
}
