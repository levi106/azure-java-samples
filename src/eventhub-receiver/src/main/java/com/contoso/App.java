package com.contoso;

import java.io.IOException;
import java.util.function.Consumer;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

public class App
{
    final static String EH_NAMESPACE_CONNECTION_STRING = System.getenv("EH_NAMESPACE_CONNECTION_STRING");
    final static String EH_HUB_NAME = System.getenv("EH_HUB_NAME");
    final static String STORAGE_CONNECTION_STRING = System.getenv("STORAGE_CONNECTION_STRING");
    final static String STORAGE_CONTAINER_NAME = System.getenv("STORAGE_CONTAINER_NAME");

    public static final Consumer<EventContext> PARTITION_PROCESSOR = eventContext -> {
        System.out.printf("Processing event from partition %s with sequence number %d with body: %s %n",
                eventContext.getPartitionContext().getPartitionId(),
                eventContext.getEventData().getSequenceNumber(),
                eventContext.getEventData().getBodyAsString());

        if (eventContext.getEventData().getSequenceNumber() % 10 == 0) {
            eventContext.updateCheckpoint();
        }
    };

    public static final Consumer<ErrorContext> ERROR_HANDLER = errorContext -> {
        System.out.printf("Error occurred in partition processor for partition %s, %s.%n",
            errorContext.getPartitionContext().getPartitionId(),
            errorContext.getThrowable());
    };

    public static void processor()
    {
        BlobContainerAsyncClient blobContainerAsyncClient = new BlobContainerClientBuilder()
            .connectionString(STORAGE_CONNECTION_STRING)
            .containerName(STORAGE_CONTAINER_NAME)
            .buildAsyncClient();

        EventProcessorClientBuilder eventProcessorClientBuilder = new EventProcessorClientBuilder()
            .connectionString(EH_NAMESPACE_CONNECTION_STRING, EH_HUB_NAME)
            .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
            .processEvent(PARTITION_PROCESSOR)
            .processError(ERROR_HANDLER)
            .checkpointStore(new BlobCheckpointStore(blobContainerAsyncClient));

        EventProcessorClient eventProcessorClient = eventProcessorClientBuilder.buildEventProcessorClient();

        System.out.println("Starting event processor");
        eventProcessorClient.start();

        try
        {
            System.out.println("Press enter to stop");
            System.in.read();
        }
        catch (IOException ex)
        {
            System.out.println("IOException!!");
        }

        System.out.println("Stopping event processor");
        eventProcessorClient.stop();
        System.out.println("Event processor stopped.");
        System.out.println("Exiting process");
    }

    public static void main( String[] args )
    {
        processor();
    }
}
