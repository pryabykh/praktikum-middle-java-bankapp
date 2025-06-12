package com.pryabykh.bankapp.exchange.service;

import com.pryabykh.bankapp.exchange.dto.RateDto;
import com.pryabykh.bankapp.exchange.repository.RateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RateService {

    private final RateRepository rateRepository;

    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Transactional(readOnly = true)
    public List<RateDto> fetchAll() {
        return rateRepository.findAll().stream().map(currency -> {
            RateDto rateDto = new RateDto();
            rateDto.setId(currency.getId());
            rateDto.setTitle(currency.getTitle());
            rateDto.setName(currency.getName());
            rateDto.setValue(currency.getValue());
            return rateDto;
        }).toList();
    }
}
