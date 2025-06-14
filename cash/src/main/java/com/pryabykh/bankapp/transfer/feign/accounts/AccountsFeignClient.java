package com.pryabykh.bankapp.transfer.feign.accounts;

import com.pryabykh.bankapp.transfer.dto.CashDto;
import com.pryabykh.bankapp.transfer.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "accounts", url = "${feign.accounts}")
public interface AccountsFeignClient {

    @PostMapping("/api/users/cash/{login}")
    ResponseDto processCash(@PathVariable("login") String login, @RequestBody CashDto cashDto);
}
