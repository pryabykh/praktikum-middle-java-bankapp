package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import com.pryabykh.bankapp.front.feign.cash.CashDto;
import com.pryabykh.bankapp.front.feign.cash.CashFeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class CashController {
    private final CashFeignClient cashFeignClient;

    public CashController(CashFeignClient cashFeignClient) {
        this.cashFeignClient = cashFeignClient;
    }

    @PostMapping("/user/cash")
    public String processCash(@ModelAttribute CashDto cashDto,
                              RedirectAttributes model,
                              Authentication authentication) {
        ResponseDto response = cashFeignClient.processCash(authentication.getName(), cashDto);
        if (response.isHasErrors()) {
            model.addFlashAttribute("cashErrors", response.getErrors());
        }
        return "redirect:/";
    }
}
