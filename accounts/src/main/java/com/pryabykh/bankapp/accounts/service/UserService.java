package com.pryabykh.bankapp.accounts.service;

import com.pryabykh.bankapp.accounts.dto.AccountSettingsDto;
import com.pryabykh.bankapp.accounts.dto.CreateUserDto;
import com.pryabykh.bankapp.accounts.dto.ResponseDto;
import com.pryabykh.bankapp.accounts.dto.UpdatePasswordDto;
import com.pryabykh.bankapp.accounts.dto.UserDto;
import com.pryabykh.bankapp.accounts.entity.User;
import com.pryabykh.bankapp.accounts.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseDto createUser(CreateUserDto createUserDto) {
        ResponseDto response = new ResponseDto();

        if (userRepository.findByLogin(createUserDto.getLogin()).isPresent()) {
            response.setHasErrors(true);
            response.getErrors().add("Такой логин уже занят. Надо бы выбрать другой");
        }
        int age = Period.between(createUserDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 18) {
            response.setHasErrors(true);
            response.getErrors().add("Вам должно быть больше 18 лет");
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
        return userRepository.findByLogin(login)
                .map(user -> new UserDto(user.getLogin(), user.getPassword(), user.getName(), user.getBirthdate()))
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

        int age = Period.between(accountSettingsDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 18) {
            response.setHasErrors(true);
            response.getErrors().add("Вам должно быть больше 18 лет");
        }

        if (response.isHasErrors()) {
            return response;
        }

        return userRepository.findByLogin(login)
                .map(user -> {
                    user.setName(accountSettingsDto.getName());
                    user.setBirthdate(accountSettingsDto.getBirthdate());
                    userRepository.save(user);
                    return response;
                })
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
