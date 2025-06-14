package com.pryabykh.bankapp.transfer.controller;

import com.pryabykh.bankapp.transfer.dto.TransferDto;
import com.pryabykh.bankapp.transfer.dto.ResponseDto;
import com.pryabykh.bankapp.transfer.service.TransferService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/{login}")
    public ResponseDto processTransfer(@PathVariable("login") String login, @RequestBody TransferDto transferDto) {
        return transferService.processTransfer(login, transferDto);
    }
}
