package com.pryabykh.bankapp.blocker.controller;

import com.pryabykh.bankapp.blocker.dto.CashDto;
import com.pryabykh.bankapp.blocker.dto.ResponseDto;
import com.pryabykh.bankapp.blocker.service.CashService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cash")
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/{login}")
    public ResponseDto processCash(@PathVariable("login") String login, @RequestBody CashDto cashDto) {
        return cashService.processCash(login, cashDto);
    }
}
