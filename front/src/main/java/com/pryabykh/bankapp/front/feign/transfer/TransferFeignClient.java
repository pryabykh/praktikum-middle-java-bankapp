package com.pryabykh.bankapp.front.feign.transfer;

import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway", contextId = "transfer")
public interface TransferFeignClient {

    @PostMapping("/transfer/api/transfer/{login}")
    ResponseDto processTransfer(@PathVariable("login") String login, @RequestBody TransferDto transferDto);
}
