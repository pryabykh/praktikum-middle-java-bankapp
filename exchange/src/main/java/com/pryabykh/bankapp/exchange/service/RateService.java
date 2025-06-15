package com.pryabykh.bankapp.exchange.service;

import com.pryabykh.bankapp.exchange.dto.RateDto;
import com.pryabykh.bankapp.exchange.dto.UpdateRandomCurrencyDto;
import com.pryabykh.bankapp.exchange.entity.Rate;
import com.pryabykh.bankapp.exchange.repository.RateRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class RateService {

    private final RateRepository rateRepository;

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
    public void updateRandomCurrency(UpdateRandomCurrencyDto randomCurrencyDto) {
        List<Rate> rates = rateRepository.findAllByBaseFalse();
        Random random = new Random();
        int randomCurrencyIndex = random.nextInt(rates.size());
        rates.get(randomCurrencyIndex).setValue(randomCurrencyDto.getValue());
        rateRepository.save(rates.get(randomCurrencyIndex));
    }
}
