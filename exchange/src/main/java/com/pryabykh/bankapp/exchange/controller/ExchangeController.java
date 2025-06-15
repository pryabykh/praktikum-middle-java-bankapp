package com.pryabykh.bankapp.exchange.controller;

import com.pryabykh.bankapp.exchange.service.ExchangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping
    public Long convert(@RequestParam("from") String from,
                        @RequestParam("to") String to,
                        @RequestParam("value") Long value) {
        return exchangeService.convert(from, to, value);
    }
}
