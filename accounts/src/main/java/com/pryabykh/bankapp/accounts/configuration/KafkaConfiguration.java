package com.pryabykh.bankapp.accounts.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public AdminClient adminClient() {
        Map<String, Object> configs = Collections.singletonMap("bootstrap.servers", bootstrapServers);
        return AdminClient.create(configs);
    }

    @Bean
    NewTopic orders() {
        return new NewTopic(
                "notifications",
                2,
                (short) 1
        );
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Configure batching and durability settings
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5); // Wait up to 5 ms for batching
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768); // Batch size of 32 KB
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensure message durability

        /*
        Prevent duplicates from retries
        sets acks=all
        */
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
