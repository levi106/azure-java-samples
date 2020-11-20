package com.samples.blobstats;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobServiceStatistics;
import com.azure.storage.blob.models.GeoReplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BlobstatsApplication {

	@RequestMapping("/")
	public String get() {
		BlobServiceClient client = new BlobServiceClientBuilder()
			.connectionString(System.getenv("STORAGE_CONNECTION_STRING"))
			.buildClient();

		BlobServiceStatistics statistics = client.getStatistics();
		GeoReplication geoReplication = statistics.getGeoReplication();
		return geoReplication.toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(BlobstatsApplication.class, args);
	}

}
