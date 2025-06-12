package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.service.UserInfoModelHelper;
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
    private final UserInfoModelHelper userInfoModelHelper;

    public MainController(AccountsFeignClient accountsFeignClient, UserInfoModelHelper userInfoModelHelper) {
        this.accountsFeignClient = accountsFeignClient;
        this.userInfoModelHelper = userInfoModelHelper;
    }

    @GetMapping()
    public String main(Model model, Authentication authentication) {
        userInfoModelHelper.addUserInfoToModel(model, authentication.getName());
        return "main";
    }
}
