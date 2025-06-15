package com.pryabykh.bankapp.notifications.controller;

import com.pryabykh.bankapp.notifications.dto.NotificationCreateDto;
import com.pryabykh.bankapp.notifications.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public void create(@RequestBody NotificationCreateDto createDto) {
        notificationService.create(createDto);
    }

    @GetMapping("/{login}")
    public List<String> fetchAll(@PathVariable("login") String login) {
        return notificationService.fetchAll(login);
    }
}
