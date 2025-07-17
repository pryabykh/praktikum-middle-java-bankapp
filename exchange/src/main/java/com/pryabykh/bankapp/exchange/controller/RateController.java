package com.pryabykh.bankapp.exchange.controller;

import com.pryabykh.bankapp.exchange.dto.RateDto;
import com.pryabykh.bankapp.exchange.dto.UpdateRandomCurrencyDto;
import com.pryabykh.bankapp.exchange.service.RateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public List<RateDto> fetchAll() {
        return rateService.fetchAll();
    }
}
