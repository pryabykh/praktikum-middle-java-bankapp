package com.pryabykh.bankapp.accounts.service;

import com.pryabykh.bankapp.accounts.dto.CreateUserDto;
import com.pryabykh.bankapp.accounts.dto.ResponseDto;
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
        int age = Period.between(createUserDto.getBirthdate(), LocalDate.now()).getYears();

        if (age < 18) {
            response.setHasErrors(true);
            response.getErrors().add("Вам должно быть больше 18 лет");
        }

        if (!createUserDto.getPassword().equals(createUserDto.getConfirmPassword())) {
            response.setHasErrors(true);
            response.getErrors().add("Пароли не совпадают");
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

    @Transactional
    public boolean updatePassword(Long userId, String newPassword) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
