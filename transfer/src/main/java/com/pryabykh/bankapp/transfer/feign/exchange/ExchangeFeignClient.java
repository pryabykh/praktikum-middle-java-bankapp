package com.pryabykh.bankapp.transfer.feign.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "exchange", url = "${feign.exchange}")
public interface ExchangeFeignClient {

    @GetMapping("/api/exchange")
    Long convert(@RequestParam("from") String from,
                 @RequestParam("to") String to,
                 @RequestParam("value") Long value);
}
