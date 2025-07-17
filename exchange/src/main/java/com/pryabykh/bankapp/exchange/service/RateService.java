package com.pryabykh.bankapp.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.bankapp.exchange.dto.RateDto;
import com.pryabykh.bankapp.exchange.dto.UpdateRandomCurrencyDto;
import com.pryabykh.bankapp.exchange.entity.Rate;
import com.pryabykh.bankapp.exchange.repository.RateRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Transactional(readOnly = true)
    public List<RateDto> fetchAll() {
        return rateRepository.findAll(Sort.by("id")).stream().map(currency -> {
            RateDto rateDto = new RateDto();
            rateDto.setId(currency.getId());
            rateDto.setTitle(currency.getTitle());
            rateDto.setName(currency.getName());
            rateDto.setValue(currency.getValue());
            return rateDto;
        }).toList();
    }

    @Transactional
    @RetryableTopic(
            // Определяем, сколько раз мы будем пытаться обработать сообщение
            attempts = "5",
            // Добавим экспоненциальную задержку, чтобы дать внешним системам время восстановиться
            backoff = @Backoff(delay = 1_00, multiplier = 2, maxDelay = 8_000),
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "exchange-generator", containerFactory = "customKafkaListenerContainerFactory")
    public void updateRandomCurrency(String randomCurrencyDto) {
        try {
            UpdateRandomCurrencyDto dto = objectMapper.readValue(randomCurrencyDto, UpdateRandomCurrencyDto.class);
            List<Rate> rates = rateRepository.findAllByBaseFalse();
            Random random = new Random();
            int randomCurrencyIndex = random.nextInt(rates.size());
            rates.get(randomCurrencyIndex).setValue(dto.getValue());
            rateRepository.save(rates.get(randomCurrencyIndex));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DltHandler
    public void handleDltMessage(ConsumerRecord<?, ?> record) {
        System.out.println("Message landed in DLT: " + record.value());
    }
}
