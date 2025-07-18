package com.pryabykh.bankapp.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.bankapp.accounts.feign.notifications.NotificationCreateDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationKafkaService {
    private final KafkaTemplate<String, String> notificationsKafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationKafkaService(KafkaTemplate<String, String> notificationsKafkaTemplate) {
        this.notificationsKafkaTemplate = notificationsKafkaTemplate;
    }

    public void create(NotificationCreateDto createDto) {
        try {
            notificationsKafkaTemplate.send(
                    "notifications",
                    UUID.randomUUID().toString(),
                    objectMapper.writeValueAsString(createDto)
            ).get();
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
