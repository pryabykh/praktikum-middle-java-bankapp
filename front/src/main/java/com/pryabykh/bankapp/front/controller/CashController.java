package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import com.pryabykh.bankapp.front.feign.cash.CashDto;
import com.pryabykh.bankapp.front.feign.cash.CashFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class CashController {
    private final CashFeignClient cashFeignClient;

    public CashController(CashFeignClient cashFeignClient) {
        this.cashFeignClient = cashFeignClient;
    }

    @PostMapping("/user/{login}/cash")
    public String processCash(@PathVariable("login") String login,
                              @ModelAttribute CashDto cashDto,
                              RedirectAttributes model) {
        ResponseDto response = cashFeignClient.processCash(login, cashDto);
        if (response.isHasErrors()) {
            model.addFlashAttribute("cashErrors", response.getErrors());
        }
        return "redirect:/";
    }
}
