package com.pryabykh.bankapp.front.feign.accounts;

import com.pryabykh.bankapp.front.dto.CreateUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway", contextId = "accounts")
public interface AccountsFeignClient {

    @PostMapping("/accounts/api/users")
    ResponseDto createUser(@RequestBody CreateUserDto createUserDto);

    @PostMapping("/accounts/api/users/auth")
    boolean authUser(@RequestParam("login") String login, @RequestParam("password") String password);

    @GetMapping("/accounts/api/users/{login}")
    UserDto fetchUserByLogin(@PathVariable("login") String login);

    @PutMapping("/accounts/api/users/{login}/updatePassword")
    ResponseDto updatePassword(@PathVariable String login,
                               @RequestBody UpdatePasswordDto updatePasswordDto);

    @PutMapping("/accounts/api/users/{login}/editUserAccounts")
    ResponseDto editUserAccounts(@PathVariable String login,
                                 @RequestBody AccountSettingsDto accountSettingsDto);

    @GetMapping("/accounts/api/users/allUsers/{login}")
    List<AllUsersDto> fetchAllUsers(@PathVariable("login") String login);
}
