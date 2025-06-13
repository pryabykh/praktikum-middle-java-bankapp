package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.dto.CreateUserDto;
import com.pryabykh.bankapp.front.feign.accounts.AccountSettingsDto;
import com.pryabykh.bankapp.front.feign.accounts.UpdatePasswordDto;
import com.pryabykh.bankapp.front.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String createUser(@ModelAttribute CreateUserDto user, RedirectAttributes model, HttpServletRequest request) {
        return userService.createUser(user, model, request);
    }

    @PostMapping("/user/editPassword")
    public String updatePassword(@ModelAttribute UpdatePasswordDto updatePasswordDto,
                                 RedirectAttributes model,
                                 Authentication authentication) {
        return userService.updatePassword(authentication.getName(), updatePasswordDto, model);
    }

    @PostMapping("/user/editUserAccounts")
    public String editUserAccounts(@ModelAttribute AccountSettingsDto accountSettingsDto,
                                   RedirectAttributes model,
                                   Authentication authentication) {
        return userService.editUserAccounts(authentication.getName(), accountSettingsDto, model);
    }
}
