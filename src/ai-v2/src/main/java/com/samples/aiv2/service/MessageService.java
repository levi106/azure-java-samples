package com.samples.aiv2.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import com.samples.aiv2.entity.Message;
import com.samples.aiv2.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void logMessage(String message) {
        Date now = Date.from(Instant.now());
        Timestamp time = new Timestamp(now.getTime());
        Message m = new Message();
        m.setMessage(message);
        m.setTime(time);
        messageRepository.save(m);
    }
}
