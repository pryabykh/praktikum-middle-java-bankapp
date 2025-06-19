package com.pryabykh.bankapp.exchangegenerator.feign.exchange;

import com.pryabykh.bankapp.exchangegenerator.dto.UpdateRandomCurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway", contextId = "exchange")
public interface ExchangeFeignClient {

    @PostMapping("/exchange/api/rates/update-random-currency")
    void updateRandomCurrency(@RequestBody UpdateRandomCurrencyDto randomCurrencyDto);
}
