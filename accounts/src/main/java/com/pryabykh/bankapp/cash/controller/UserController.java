package com.pryabykh.bankapp.cash.controller;

import com.pryabykh.bankapp.cash.dto.AccountSettingsDto;
import com.pryabykh.bankapp.cash.dto.CashDto;
import com.pryabykh.bankapp.cash.dto.CreateUserDto;
import com.pryabykh.bankapp.cash.dto.ResponseDto;
import com.pryabykh.bankapp.cash.dto.UpdatePasswordDto;
import com.pryabykh.bankapp.cash.dto.UserDto;
import com.pryabykh.bankapp.cash.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseDto createUser(@RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @GetMapping("/{login}")
    public UserDto fetchUserByLogin(@PathVariable("login") String login) {
        return userService.fetchUserByLogin(login);
    }

    @PutMapping("/{login}/updatePassword")
    public ResponseDto updatePassword(@PathVariable String login,
                                      @RequestBody UpdatePasswordDto updatePasswordDto) {
        return userService.updatePassword(login, updatePasswordDto);
    }

    @PutMapping("/{login}/editUserAccounts")
    public ResponseDto editUserAccounts(@PathVariable String login,
                                        @RequestBody AccountSettingsDto accountSettingsDto) {
        return userService.editUserAccounts(login, accountSettingsDto);
    }

    @PostMapping("/{login}")
    public ResponseDto processCash(@PathVariable("login") String login, @RequestBody CashDto cashDto) {
        return userService.processCash(login, cashDto);
    }
}
