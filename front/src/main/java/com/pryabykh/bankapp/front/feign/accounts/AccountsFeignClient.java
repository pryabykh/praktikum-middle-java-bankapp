package com.pryabykh.bankapp.front.feign.accounts;

import com.pryabykh.bankapp.front.dto.CreateUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "accounts", url = "${feign.accounts}")
public interface AccountsFeignClient {

    @PostMapping("/api/users")
    ResponseDto createUser(@RequestBody CreateUserDto createUserDto);

    @PostMapping("/api/users/auth")
    boolean authUser(@RequestParam("login") String login, @RequestParam("password") String password);

    @GetMapping("/api/users/{login}")
    UserDto fetchUserByLogin(@PathVariable("login") String login);

    @PutMapping("/api/users/{login}")
    ResponseDto updatePassword(@PathVariable String login,
                               @RequestBody UpdatePasswordDto updatePasswordDto);
}
