package com.samples.aiv2.controller;

import com.samples.aiv2.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("echo")
public class EchoController {
    private static final Logger LOG = LoggerFactory.getLogger(EchoController.class);

    @Autowired
    private MessageService messageService;

    @RequestMapping(method=RequestMethod.GET)
    public String get(@RequestParam(name="message", defaultValue="hello") String message) {
        LOG.info("message: {}", message);
        messageService.logMessage(message);
        return message;
    }
}
