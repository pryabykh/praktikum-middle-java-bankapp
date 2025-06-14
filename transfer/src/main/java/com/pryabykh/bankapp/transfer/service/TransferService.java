package com.pryabykh.bankapp.transfer.service;

import com.pryabykh.bankapp.transfer.dto.TransferDto;
import com.pryabykh.bankapp.transfer.dto.ResponseDto;
import com.pryabykh.bankapp.transfer.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.transfer.feign.exchange.ExchangeFeignClient;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    private final AccountsFeignClient accountsFeignClient;
    private final ExchangeFeignClient exchangeFeignClient;

    public TransferService(AccountsFeignClient accountsFeignClient, ExchangeFeignClient exchangeFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
        this.exchangeFeignClient = exchangeFeignClient;
    }

    public ResponseDto processTransfer(String login, TransferDto transferDto) {
        Long convertedValue = exchangeFeignClient.convert(transferDto.getFromCurrency(), transferDto.getToCurrency(), transferDto.getValue() * 100);
        transferDto.setConvertedValue(convertedValue);
        return accountsFeignClient.processTransfer(login, transferDto);
    }
}
