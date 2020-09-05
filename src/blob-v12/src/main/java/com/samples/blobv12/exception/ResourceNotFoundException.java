package com.samples.blobv12.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 4513302502079988706L;

    public ResourceNotFoundException(String exception) {
        super(exception);
    }
}