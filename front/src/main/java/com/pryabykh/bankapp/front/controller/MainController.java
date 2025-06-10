package com.pryabykh.bankapp.front.controller;

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

    @GetMapping()
    public String main(Model model, Authentication authentication) {
        model.addAttribute("login", authentication.getName());
        return "main";
    }
}
