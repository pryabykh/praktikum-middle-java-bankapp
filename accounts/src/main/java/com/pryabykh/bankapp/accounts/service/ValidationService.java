package com.pryabykh.bankapp.accounts.service;

import com.pryabykh.bankapp.accounts.dto.CashDto;
import com.pryabykh.bankapp.accounts.dto.TransferDto;
import com.pryabykh.bankapp.accounts.entity.Account;
import com.pryabykh.bankapp.accounts.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService {

    @Transactional
    public List<String> validate(CashDto cashDto, User user) {
        List<String> errors = new ArrayList<>();
        if (cashDto.getCurrency() == null || cashDto.getCurrency().isBlank()) {
            errors.add("Валюта должна быть заполнена");
        }
        if (null == cashDto.getValue()) {
            errors.add("Количество денег не может быть пустым");
        } else if (cashDto.getValue() <= 0) {
            errors.add("Некорректно количество денег");
        }
        if (null == cashDto.getAction() || cashDto.getAction().isBlank()) {
            errors.add("Действие с деньгами не может быть пустым");
        } else if (!List.of("GET", "PUT").contains(cashDto.getAction())) {
            errors.add("Некорректное действие со счетом");
        }

        List<Account> userAccounts = user.getAccounts();

        if (userAccounts.stream().noneMatch(a -> a.getCurrency().equals(cashDto.getCurrency()))) {
            errors.add("Счет с валютой " + cashDto.getCurrency() + " не найден");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        Account accountToModify = userAccounts.stream()
                .filter(a -> a.getCurrency().equals(cashDto.getCurrency()))
                .findFirst()
                .orElseThrow();

        if ("GET".equals(cashDto.getAction()) && (accountToModify.getBalance() - (cashDto.getValue() * 100)) < 0) {
            errors.add("Недостаточно средств");
        }
        
        return errors;
    }

    @Transactional
    public List<String> validate(TransferDto transferDto, User user, User toUser) {
        List<String> errors = new ArrayList<>();

        if (transferDto.getFromCurrency() == null || transferDto.getFromCurrency().isBlank()) {
            errors.add("Валюта Со счета должна быть заполнена");
        }
        if (transferDto.getToCurrency() == null || transferDto.getToCurrency().isBlank()) {
            errors.add("Валюта На счет должна быть заполнена");
        }
        if (transferDto.getToLogin() == null || transferDto.getToLogin().isBlank()) {
            errors.add("Получатель должен быть заполнен");
        }
        if (null == transferDto.getValue()) {
            errors.add("Количество денег не может быть пустым");
        } else if (transferDto.getValue() <= 0) {
            errors.add("Некорректно количество денег");
        }
        if (user.getLogin().equals(transferDto.getToLogin()) && transferDto.getFromCurrency().equals(transferDto.getToCurrency())) {
            errors.add("Перевести можно только между разными счетами");
        }

        List<Account> userAccounts = user.getAccounts();

        if (userAccounts.stream().noneMatch(a -> a.getCurrency().equals(transferDto.getFromCurrency()))) {
            errors.add("У вас не открыт счет с валютой " + transferDto.getFromCurrency() + " не найден");
        }
        List<Account> toUserAccounts = toUser.getAccounts();

        if (toUserAccounts.stream().noneMatch(a -> a.getCurrency().equals(transferDto.getToCurrency()))) {
            errors.add("У пользователя " + toUser.getName() + " нет счета в выбранной валюте");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        Account accountFrom = userAccounts.stream()
                .filter(a -> a.getCurrency().equals(transferDto.getFromCurrency()))
                .findFirst()
                .orElseThrow();

        if ((accountFrom.getBalance() - (transferDto.getValue() * 100)) < 0) {
            errors.add("Недостаточно средств");
        }

        return errors;
    }
}
