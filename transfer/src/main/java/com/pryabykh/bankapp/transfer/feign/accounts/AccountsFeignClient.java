package com.pryabykh.bankapp.transfer.feign.accounts;

import com.pryabykh.bankapp.transfer.dto.TransferDto;
import com.pryabykh.bankapp.transfer.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "accounts", url = "${feign.accounts}")
public interface AccountsFeignClient {

    @PostMapping("/api/users/transfer/{login}")
    ResponseDto processTransfer(@PathVariable("login") String login, @RequestBody TransferDto transferDto);
}
