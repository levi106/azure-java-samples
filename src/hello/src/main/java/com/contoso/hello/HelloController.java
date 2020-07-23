package com.contoso.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/")
    public String root() {
        return "Ok";
    }
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
    @RequestMapping("/health")
    public String health() {
        return "Ok";
    }
}