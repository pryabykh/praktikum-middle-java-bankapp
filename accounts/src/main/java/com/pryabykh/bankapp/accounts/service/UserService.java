package com.pryabykh.bankapp.accounts.service;

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
    public User createUser(String login, String password, String name, String birthdate) {
        LocalDate parsedBirthdate = LocalDate.parse(birthdate);
        int age = Period.between(parsedBirthdate, LocalDate.now()).getYears();

        if (age < 18) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setBirthdate(parsedBirthdate);

        return userRepository.save(user);
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
