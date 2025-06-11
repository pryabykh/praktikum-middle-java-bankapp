package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.feign.accounts.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final AccountsFeignClient accountsFeignClient;

    public MainController(AccountsFeignClient accountsFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
    }

    @GetMapping()
    public String main(Model model, Authentication authentication) {
        model.addAttribute("login", authentication.getName());
        UserDto user = accountsFeignClient.fetchUserByLogin(authentication.getName());
        model.addAttribute("name", user.getName());
        model.addAttribute("birthdate", user.getBirthdate());
        return "main";
    }
}
