package com.pryabykh.bankapp.exchange.service;

import com.pryabykh.bankapp.exchange.dto.AccountDto;
import com.pryabykh.bankapp.exchange.dto.AccountSettingsDto;
import com.pryabykh.bankapp.exchange.dto.CreateUserDto;
import com.pryabykh.bankapp.exchange.dto.ResponseDto;
import com.pryabykh.bankapp.exchange.dto.UpdatePasswordDto;
import com.pryabykh.bankapp.exchange.dto.UserDto;
import com.pryabykh.bankapp.exchange.entity.Account;
import com.pryabykh.bankapp.exchange.entity.User;
import com.pryabykh.bankapp.exchange.feign.exchange.ExchangeFeignClient;
import com.pryabykh.bankapp.exchange.feign.exchange.RateDto;
import com.pryabykh.bankapp.exchange.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ExchangeFeignClient exchangeFeignClient;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ExchangeFeignClient exchangeFeignClient) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.exchangeFeignClient = exchangeFeignClient;
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
    public UserDto fetchUserByLogin(String login) {
        List<RateDto> rates = exchangeFeignClient.fetchAll();
        return userRepository.findByLogin(login)
                .map(user -> {
                    UserDto userDto = new UserDto(user.getLogin(), user.getPassword(), user.getName(), user.getBirthdate());
                    List<Account> accounts = user.getAccounts();
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
        return response;
    }
}
