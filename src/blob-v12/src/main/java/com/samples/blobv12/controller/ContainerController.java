package com.samples.blobv12.controller;

import java.util.List;

import com.samples.blobv12.service.BlobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("container")
public class ContainerController {
    @Autowired
    private BlobService blobService;

    @GetMapping("/{container}")
    public List<String> get(@PathVariable() String container) {
        List<String> files = this.blobService.listBlobs(container);
        return files;
    }

    @PostMapping("/{container}")
    public void post(@PathVariable() String container) {
        this.blobService.createContainer(container);
    }
}