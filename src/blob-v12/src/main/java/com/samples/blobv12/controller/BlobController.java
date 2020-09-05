package com.samples.blobv12.controller;

import java.io.IOException;
import java.io.InputStream;

import com.samples.blobv12.service.BlobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("blob")
public class BlobController {
    @Autowired
    private BlobService blobService;
   
    @RequestMapping(value="/{container}/{blobName}", consumes={"multipart/form-data"}, method=RequestMethod.POST)
    public void post(@PathVariable() String container, @PathVariable() String blobName, @RequestParam("file") MultipartFile file) {
        try {
            InputStream data = file.getInputStream();
            long length = file.getSize();
            blobService.upload(container, blobName, data, length);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}