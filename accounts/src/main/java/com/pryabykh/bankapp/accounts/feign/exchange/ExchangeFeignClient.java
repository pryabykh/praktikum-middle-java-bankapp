package com.pryabykh.bankapp.accounts.feign.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "gateway", contextId = "exchange")
public interface ExchangeFeignClient {

    @GetMapping("/exchange/api/rates")
    List<RateDto> fetchAll();
}
