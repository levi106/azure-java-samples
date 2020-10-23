package com.samples.aiv2.controller;

import java.util.stream.Collectors;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import com.microsoft.applicationinsights.telemetry.TelemetryContext;
import com.microsoft.applicationinsights.web.internal.RequestTelemetryContext;
import com.microsoft.applicationinsights.web.internal.ThreadContext;
import com.samples.aiv2.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public String get(@RequestParam(name="message", defaultValue="hello") String message, @RequestHeader MultiValueMap<String,String> headers) {
        LOG.info("message: {}", message);
        headers.forEach((key, value) -> {
            LOG.info(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        RequestTelemetryContext context = ThreadContext.getRequestTelemetryContext();
        RequestTelemetry requestTelemetry = context.getHttpRequestTelemetry();
        LOG.info("id: {}", requestTelemetry.getId());
        LOG.info("operation.id: {}", requestTelemetry.getContext().getOperation().getId());
        LOG.info("operation.parentId: {}", requestTelemetry.getContext().getOperation().getParentId());
        messageService.logMessage(message);
        return message;
    }
}
