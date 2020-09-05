package com.samples.blobv12.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

import org.springframework.stereotype.Service;

import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.context.Scope;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;

@Service
public class BlobService {
    private static final Tracer TRACER = OpenTelemetry.getTracer("com.samples.blobv12.service.BlobService");
    private final BlobServiceClient serviceClient;

    public BlobService() {
        this.serviceClient = new BlobServiceClientBuilder()
                .connectionString(System.getenv("STORAGE_CONNECTION_STRING"))
                .buildClient();
    }

    public void createContainer(String container) {
        Span span = TRACER.spanBuilder("create-container").startSpan();
        try (final Scope scope = TRACER.withSpan(span)) {
            BlobContainerClient containerClient = serviceClient.getBlobContainerClient(container);
            if (containerClient.exists()) {
                throw new com.samples.blobv12.exception.AlreadyExistsException(String.format("%s already exists.", container));
            }
            containerClient.create();
        } finally {
            span.end();
        }
    }

    public List<String> listBlobs(String container) {
        ArrayList<String> files = new ArrayList<String>();

        Span span = TRACER.spanBuilder("list-blobas").startSpan();
        try (final Scope scope = TRACER.withSpan(span)) {
            BlobContainerClient containerClient = serviceClient.getBlobContainerClient(container);
            if (!containerClient.exists()) {
                throw new com.samples.blobv12.exception.ResourceNotFoundException(String.format("Container %s does not exist.", container));
            }
            containerClient.listBlobs()
            .forEach(blobItem -> {
                files.add(blobItem.getName());
            });
        } finally {
            span.end();
        }
        return files;
    }

    public void upload(String container, String blobName, InputStream data, long length) {
        Span span = TRACER.spanBuilder("upload").startSpan();
        try (final Scope scope = TRACER.withSpan(span)) {
            BlobContainerClient containerClient = serviceClient.getBlobContainerClient(container);
            if (!containerClient.exists()) {
                throw new com.samples.blobv12.exception.ResourceNotFoundException(String.format("Container %s does not exists.", container));
            }
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.upload(data, length);
        } finally {
            span.end();
        }
    }
}