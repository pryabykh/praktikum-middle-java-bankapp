package com.pryabykh.bankapp.exchangegenerator.service;

import com.pryabykh.bankapp.exchangegenerator.dto.UpdateRandomCurrencyDto;
import com.pryabykh.bankapp.exchangegenerator.feign.exchange.ExchangeFeignClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UpdateRandomCurrencyService {
    private final ExchangeGeneratorKafkaService exchangeGeneratorKafkaService;

    public UpdateRandomCurrencyService(ExchangeGeneratorKafkaService exchangeGeneratorKafkaService) {
        this.exchangeGeneratorKafkaService = exchangeGeneratorKafkaService;
    }

    @Scheduled(fixedDelay = 1000)
    public void update() {
        UpdateRandomCurrencyDto updateRandomCurrencyDto = new UpdateRandomCurrencyDto();
        Random random = new Random();
        updateRandomCurrencyDto.setValue(random.nextInt(99) + 1);
        exchangeGeneratorKafkaService.updateRandomCurrency(updateRandomCurrencyDto);
    }
}
