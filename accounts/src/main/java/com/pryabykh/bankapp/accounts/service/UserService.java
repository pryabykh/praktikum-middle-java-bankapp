package com.pryabykh.bankapp.accounts.service;

import com.pryabykh.bankapp.accounts.dto.AccountDto;
import com.pryabykh.bankapp.accounts.dto.AccountSettingsDto;
import com.pryabykh.bankapp.accounts.dto.AllUsersDto;
import com.pryabykh.bankapp.accounts.dto.CashDto;
import com.pryabykh.bankapp.accounts.dto.CreateUserDto;
import com.pryabykh.bankapp.accounts.dto.ResponseDto;
import com.pryabykh.bankapp.accounts.dto.TransferDto;
import com.pryabykh.bankapp.accounts.dto.UpdatePasswordDto;
import com.pryabykh.bankapp.accounts.dto.UserDto;
import com.pryabykh.bankapp.accounts.entity.Account;
import com.pryabykh.bankapp.accounts.entity.User;
import com.pryabykh.bankapp.accounts.feign.exchange.ExchangeFeignClient;
import com.pryabykh.bankapp.accounts.feign.exchange.RateDto;
import com.pryabykh.bankapp.accounts.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NotificationsOutboxService notificationsOutboxService;
    private final ExchangeFeignClient exchangeFeignClient;
    private final ValidationService validationService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, NotificationsOutboxService notificationsOutboxService, ExchangeFeignClient exchangeFeignClient, ValidationService validationService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.notificationsOutboxService = notificationsOutboxService;
        this.exchangeFeignClient = exchangeFeignClient;
        this.validationService = validationService;
    }

    @Transactional
    public ResponseDto createUser(CreateUserDto createUserDto) {
        ResponseDto response = new ResponseDto();

        if (userRepository.findByLogin(createUserDto.getLogin()).isPresent()) {
            response.setHasErrors(true);
            response.getErrors().add("Такой логин уже занят. Надо бы выбрать другой");
        }

        if (createUserDto.getBirthdate() == null) {
            response.setHasErrors(true);
            response.getErrors().add("Дата рождения не должна быть пустой");
        } else {
            int age = Period.between(createUserDto.getBirthdate(), LocalDate.now()).getYears();
            if (age < 18) {
                response.setHasErrors(true);
                response.getErrors().add("Вам должно быть больше 18 лет");
            }
        }

        if (createUserDto.getName() == null || createUserDto.getName().isBlank()) {
            response.setHasErrors(true);
            response.getErrors().add("Имя не должно быть пустым");
        }

        if (createUserDto.getPassword() == null || createUserDto.getPassword().isBlank() ||
                createUserDto.getConfirmPassword() == null || createUserDto.getConfirmPassword().isBlank()) {
            response.setHasErrors(true);
            response.getErrors().add("Пароль не может быть пустым");
        } else {
            if (!createUserDto.getPassword().equals(createUserDto.getConfirmPassword())) {
                response.setHasErrors(true);
                response.getErrors().add("Пароли не совпадают");
            }
        }

        if (response.isHasErrors()) {
            return response;
        }

        User user = new User();
        user.setLogin(createUserDto.getLogin());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setName(createUserDto.getName());
        user.setBirthdate(createUserDto.getBirthdate());

        userRepository.save(user);

        return response;
    }

    @Transactional(readOnly = true)
    public List<AllUsersDto> fetchAllUsers(String login) {
        List<User> users = userRepository.findAll().stream().filter(u -> !u.getLogin().equals(login)).toList();
        return users.stream().map(user -> {
                    AllUsersDto userDto = new AllUsersDto(user.getLogin(), user.getName());
                    return userDto;
                }).toList();
    }

    @Transactional(readOnly = true)
    public UserDto fetchUserByLogin(String login) {
        List<RateDto> rates = exchangeFeignClient.fetchAll();
        return userRepository.findByLogin(login)
                .map(user -> {
                    UserDto userDto = new UserDto(user.getLogin(), user.getPassword(), user.getName(), user.getBirthdate());
                    List<Account> accounts = user.getAccounts().stream().sorted(Comparator.comparing(Account::getId)).toList();
                    rates.forEach(rate -> {
                        AccountDto accountDto = new AccountDto();
                        accountDto.setCurrency(rate);
                        accounts.stream().filter(a -> a.getCurrency().equals(rate.getName())).findFirst().ifPresent(a -> {
                            accountDto.setExists(true);
                            accountDto.setBalance(a.getBalance());
                        });
                        userDto.getAccounts().add(accountDto);
                    });
                    return userDto;
                })
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public ResponseDto updatePassword(String login, UpdatePasswordDto updatePasswordDto) {
        ResponseDto response = new ResponseDto();
        if (updatePasswordDto.getPassword() == null || updatePasswordDto.getPassword().isBlank() ||
                updatePasswordDto.getConfirmPassword() == null || updatePasswordDto.getConfirmPassword().isBlank()) {
            response.setHasErrors(true);
            response.getErrors().add("Пароль не может быть пустым");
        } else {
            if (!updatePasswordDto.getPassword().equals(updatePasswordDto.getConfirmPassword())) {
                response.setHasErrors(true);
                response.getErrors().add("Пароли не совпадают");
            }
        }

        if (response.isHasErrors()) {
            return response;
        }

        return userRepository.findByLogin(login)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
                    userRepository.save(user);
                    notificationsOutboxService.createNotification(login, "Пароль успешно обновлен");
                    return response;
                })
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public ResponseDto editUserAccounts(String login, AccountSettingsDto accountSettingsDto) {
        ResponseDto response = new ResponseDto();

        if (accountSettingsDto.getBirthdate() == null) {
            response.getErrors().add("Дата рождения не должна быть пустой");
        } else {
            int age = Period.between(accountSettingsDto.getBirthdate(), LocalDate.now()).getYears();
            if (age < 18) {
                response.getErrors().add("Вам должно быть больше 18 лет");
            }
        }

        if (accountSettingsDto.getName() == null || accountSettingsDto.getName().isBlank()) {
            response.getErrors().add("Имя не должно быть пустым");
        }

        User user = userRepository.findByLogin(login).orElseThrow(() -> new IllegalArgumentException());
        List<Account> userAccounts = user.getAccounts();

        List<Account> accountsToRemove = new ArrayList<>();
        for (Account userAccount : userAccounts) {
            if (!accountSettingsDto.getAccounts().contains(userAccount.getCurrency())) {
                if (userAccount.getBalance() != 0) {
                    response.getErrors().add("Чтобы закрыть счет " + userAccount.getCurrency() + " нужно чтобы баланс был равен 0");
                } else {
                    accountsToRemove.add(userAccount);
                }
            }
        }

        if (!response.getErrors().isEmpty()) {
            response.setHasErrors(true);
            return response;
        }

        for (Account accountToRemove : accountsToRemove) {
            userAccounts.remove(accountToRemove);
            accountToRemove.setUser(null);
        }

        for (String currencyName : accountSettingsDto.getAccounts()) {
            if (userAccounts.stream().noneMatch(a -> a.getCurrency().equals(currencyName))) {
                Account newAccount = new Account();
                newAccount.setBalance(0L);
                newAccount.setUser(user);
                newAccount.setCurrency(currencyName);
                user.getAccounts().add(newAccount);
            }
        }

        user.setName(accountSettingsDto.getName());
        user.setBirthdate(accountSettingsDto.getBirthdate());
        userRepository.save(user);
        notificationsOutboxService.createNotification(login, "Данные аккаунта отредактированы");
        return response;
    }

    @Transactional
    public ResponseDto processCash(String login, CashDto cashDto) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new IllegalArgumentException());
        ResponseDto response = new ResponseDto();
        List<String> errors = validationService.validate(cashDto, user);

        if (!errors.isEmpty()) {
            response.getErrors().addAll(errors);
            response.setHasErrors(true);
            return response;
        }

        Account accountToModify = user.getAccounts().stream()
                .filter(a -> a.getCurrency().equals(cashDto.getCurrency()))
                .findFirst()
                .orElseThrow();

        if ("GET".equals(cashDto.getAction())) {
            notificationsOutboxService.createNotification(login, "Снятие наличных: " + cashDto.getValue() + " " + cashDto.getCurrency());
            accountToModify.setBalance(accountToModify.getBalance() - cashDto.getValue() * 100);
        }
        if ("PUT".equals(cashDto.getAction())) {
            notificationsOutboxService.createNotification(login, "Зачисление наличных: " + cashDto.getValue() + " " + cashDto.getCurrency());
            accountToModify.setBalance(accountToModify.getBalance() + cashDto.getValue() * 100);
        }
        userRepository.save(user);
        return response;
    }

    @Transactional
    public ResponseDto processTransfer(String login, TransferDto transferDto) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new IllegalArgumentException());
        List<Account> userAccounts = user.getAccounts();
        User toUser = userRepository.findByLogin(transferDto.getToLogin()).orElseThrow(() -> new IllegalArgumentException());
        List<Account> toUserAccounts = toUser.getAccounts();
        ResponseDto response = new ResponseDto();

        List<String> errors = validationService.validate(transferDto, user, toUser);

        if (!errors.isEmpty()) {
            response.getErrors().addAll(errors);
            response.setHasErrors(true);
            return response;
        }
        Account accountFrom = userAccounts.stream()
                .filter(a -> a.getCurrency().equals(transferDto.getFromCurrency()))
                .findFirst()
                .orElseThrow();

        Account accountTo = toUserAccounts.stream()
                .filter(a -> a.getCurrency().equals(transferDto.getToCurrency()))
                .findFirst()
                .orElseThrow();

        accountFrom.setBalance(accountFrom.getBalance() - transferDto.getValue() * 100);
        accountTo.setBalance(accountTo.getBalance() + transferDto.getConvertedValue());
        userRepository.save(user);
        if (!login.equals(transferDto.getToLogin())) {
            notificationsOutboxService.createNotification(login, "Перевод " + transferDto.getValue() + " " + transferDto.getFromCurrency() + " -> " + transferDto.getToCurrency() + " (пользователю " + transferDto.getToLogin() + ")");
            notificationsOutboxService.createNotification(transferDto.getToLogin(), "Зачисление " + transferDto.getValue() + " " + transferDto.getFromCurrency() + " -> " + transferDto.getToCurrency() + " (от пользователя " + login + ")");
            userRepository.save(toUser);
        } else {
            notificationsOutboxService.createNotification(login, "Перевод " + transferDto.getValue() + " " + transferDto.getFromCurrency() + " -> " + transferDto.getToCurrency() + " (на свой счет)");
        }
        return response;
    }
}
