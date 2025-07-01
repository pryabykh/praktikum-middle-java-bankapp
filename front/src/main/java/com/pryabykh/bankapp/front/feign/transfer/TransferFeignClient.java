package com.pryabykh.bankapp.front.feign.transfer;

import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "transfer", url = "${feign.transfer}")
public interface TransferFeignClient {

    @PostMapping("/api/transfer/{login}")
    ResponseDto processTransfer(@PathVariable("login") String login, @RequestBody TransferDto transferDto);
}
