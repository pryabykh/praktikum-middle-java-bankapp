package com.pryabykh.bankapp.notifications.controller;

import com.pryabykh.bankapp.notifications.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{login}")
    public List<String> fetchAll(@PathVariable("login") String login) {
        return notificationService.fetchAll(login);
    }
}
