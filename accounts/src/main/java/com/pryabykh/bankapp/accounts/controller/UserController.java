package com.pryabykh.bankapp.accounts.controller;

import com.pryabykh.bankapp.accounts.dto.CreateUserDto;
import com.pryabykh.bankapp.accounts.dto.ResponseDto;
import com.pryabykh.bankapp.accounts.service.UserService;
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

    @PutMapping("/{userId}")
    public String updatePassword(@PathVariable Long userId,
                                 @RequestBody String newPassword) {
        boolean updated = userService.updatePassword(userId, newPassword);
        if (updated) {
            return "Password updated successfully";
        } else {
            return "User not found";
        }
    }
}
