package com.pryabykh.bankapp.front.feign.cash;

import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import com.pryabykh.bankapp.front.feign.exchange.RateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "gateway", contextId = "cash")
public interface CashFeignClient {

    @PostMapping("/cash/api/cash/{login}")
    ResponseDto processCash(@PathVariable("login") String login, @RequestBody CashDto cashDto);
}
