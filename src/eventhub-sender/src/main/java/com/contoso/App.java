package com.contoso;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import com.azure.core.amqp.AmqpRetryOptions;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;

public class App
{
    final static String EH_NAMESPACE_CONNECTION_STRING = System.getenv("EH_NAMESPACE_CONNECTION_STRING");
    final static String EH_HUB_NAME = System.getenv("EH_HUB_NAME");
    final static int AMQP_TRY_TIMEOUT = Integer.parseInt(System.getenv("AMQP_TRY_TIMEOUT"));
    final static int INTERVAL = Integer.parseInt(System.getenv("INTERVAL"));

    public static void batchSend()
    {
        AmqpRetryOptions options = new AmqpRetryOptions();
        options.setTryTimeout(Duration.ofMillis(AMQP_TRY_TIMEOUT));

        EventHubProducerClient producer = new EventHubClientBuilder()
            .retry(options)
            .connectionString(EH_NAMESPACE_CONNECTION_STRING, EH_HUB_NAME)
            .buildProducerClient();

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            while (true)
            {
                EventDataBatch batch = producer.createBatch();
                Date date = new Date(System.currentTimeMillis());
                String body = sdf.format(date);
                batch.tryAdd(new EventData(body));
                producer.send(batch);
                System.out.printf("Message: %s\n", body);
                Thread.sleep(INTERVAL);
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("interrupted");
        }
        producer.close();
    }

    public static void main( String[] args )
    {
        System.out.printf("EH_NAMESPACE_CONNECTION_STRING: %s\n", EH_NAMESPACE_CONNECTION_STRING);
        System.out.printf("EH_HUB_NAME: %s\n", EH_HUB_NAME);
        System.out.printf("AMQP_TRY_TIMEOUT: %d\n", AMQP_TRY_TIMEOUT);
        System.out.printf("INTERVAL: %d\n", INTERVAL);
        batchSend();
    }
}
