package com.contoso.hello;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/")
    public String root() {
        logger.debug("root");
        return "Ok";
    }
    @RequestMapping("/hello")
    public String hello(@RequestHeader Map<String, String> headers) {
        logger.debug("hello");
        headers.forEach((key, value) -> {
            logger.debug(String.format("Header '%s' = %s", key, value));
        });
        return "Hello World";
    }
    @RequestMapping("/health")
    public String health() {
        logger.debug("health");
        return "Ok";
    }
}