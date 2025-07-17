package com.pryabykh.bankapp.exchangegenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.bankapp.exchangegenerator.dto.UpdateRandomCurrencyDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ExchangeGeneratorKafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExchangeGeneratorKafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void updateRandomCurrency(UpdateRandomCurrencyDto createDto) {
        try {
            kafkaTemplate.send(
                    "exchange-generator",
                    UUID.randomUUID().toString(),
                    objectMapper.writeValueAsString(createDto)
            ).get();
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
