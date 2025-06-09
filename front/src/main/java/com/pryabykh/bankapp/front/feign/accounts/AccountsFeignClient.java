package com.pryabykh.bankapp.front.feign.accounts;

import com.pryabykh.bankapp.front.dto.CreateUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "accounts", url = "${feign.accounts}")
public interface AccountsFeignClient {

    @PostMapping("/api/users")
    ResponseDto createUser(@RequestBody CreateUserDto createUserDto);
}
