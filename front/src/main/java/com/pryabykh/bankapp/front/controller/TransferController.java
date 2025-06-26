package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.accounts.ResponseDto;
import com.pryabykh.bankapp.front.feign.transfer.TransferDto;
import com.pryabykh.bankapp.front.feign.transfer.TransferFeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class TransferController {
    private final TransferFeignClient transferFeignClient;

    public TransferController(TransferFeignClient transferFeignClient) {
        this.transferFeignClient = transferFeignClient;
    }

    @PostMapping("/user/transfer")
    public String processCash(@ModelAttribute TransferDto transferDto,
                              RedirectAttributes model,
                              Authentication authentication) {
        ResponseDto response = transferFeignClient.processTransfer(authentication.getName(), transferDto);
        if (response.isHasErrors()) {
            model.addFlashAttribute("transferErrors", response.getErrors());
        }
        return "redirect:/";
    }
}
