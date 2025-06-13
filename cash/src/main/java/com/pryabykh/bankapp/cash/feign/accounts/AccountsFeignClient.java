package com.pryabykh.bankapp.cash.feign.accounts;

import com.pryabykh.bankapp.cash.dto.CashDto;
import com.pryabykh.bankapp.cash.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "accounts", url = "${feign.accounts}")
public interface AccountsFeignClient {

    @PostMapping("/api/users/{login}")
    ResponseDto processCash(@PathVariable("login") String login, @RequestBody CashDto cashDto);
}
