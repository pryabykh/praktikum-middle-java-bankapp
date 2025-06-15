package com.pryabykh.bankapp.transfer.service;

import com.pryabykh.bankapp.transfer.dto.TransferDto;
import com.pryabykh.bankapp.transfer.dto.ResponseDto;
import com.pryabykh.bankapp.transfer.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.transfer.feign.blocker.BlockerFeignClient;
import com.pryabykh.bankapp.transfer.feign.exchange.ExchangeFeignClient;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    private final AccountsFeignClient accountsFeignClient;
    private final ExchangeFeignClient exchangeFeignClient;
    private final BlockerFeignClient blockerFeignClient;

    public TransferService(AccountsFeignClient accountsFeignClient, ExchangeFeignClient exchangeFeignClient, BlockerFeignClient blockerFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
        this.exchangeFeignClient = exchangeFeignClient;
        this.blockerFeignClient = blockerFeignClient;
    }

    public ResponseDto processTransfer(String login, TransferDto transferDto) {
        boolean suspicious = blockerFeignClient.isSuspicious();
        if (!suspicious) {
            Long convertedValue = exchangeFeignClient.convert(transferDto.getFromCurrency(), transferDto.getToCurrency(), transferDto.getValue() * 100);
            transferDto.setConvertedValue(convertedValue);
            return accountsFeignClient.processTransfer(login, transferDto);
        } else {
            ResponseDto response = new ResponseDto();
            response.setHasErrors(true);
            response.getErrors().add("⛔⛔⛔ Подозрительная операция заблокирована ⛔⛔⛔");
            return response;
        }
    }
}
