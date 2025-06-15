package com.pryabykh.bankapp.front.controller;

import com.pryabykh.bankapp.front.feign.notifications.NotificationsFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationsFeignClient notificationsFeignClient;

    public NotificationsController(NotificationsFeignClient notificationsFeignClient) {
        this.notificationsFeignClient = notificationsFeignClient;
    }

    @GetMapping(value = "/api/notifications", produces = "application/json")
    public List<String> fetchAll(Authentication authentication) {
        return notificationsFeignClient.fetchAll(authentication.getName());
    }
}
