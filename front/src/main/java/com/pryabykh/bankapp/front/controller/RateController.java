package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.exchange.ExchangeFeignClient;
import com.pryabykh.bankapp.front.feign.exchange.RateDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    private final ExchangeFeignClient exchangeFeignClient;

    public RateController(ExchangeFeignClient exchangeFeignClient) {
        this.exchangeFeignClient = exchangeFeignClient;
    }

    @GetMapping
    public List<RateDto> fetchAll() {
        return exchangeFeignClient.fetchAll();
    }
}
