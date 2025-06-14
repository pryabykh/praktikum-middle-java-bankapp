package com.pryabykh.bankapp.transfer.service;

import com.pryabykh.bankapp.transfer.entity.Rate;
import com.pryabykh.bankapp.transfer.repository.RateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExchangeService {

    private final RateRepository rateRepository;

    public ExchangeService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Transactional(readOnly = true)
    public Long convert(String from, String to, Long value) {
        List<Rate> rates = rateRepository.findAll();
        Rate fromRate = rates.stream().filter(r -> r.getName().equals(from)).findFirst().orElseThrow();
        Rate toRate = rates.stream().filter(r -> r.getName().equals(to)).findFirst().orElseThrow();
        if (fromRate.isBase() && toRate.isBase()) {
            return value;
        }
        return value / fromRate.getValue() * toRate.getValue();
    }
}
