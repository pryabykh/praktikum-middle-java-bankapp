package com.pryabykh.bankapp.front.feign.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "gateway", contextId = "notifications")
public interface NotificationsFeignClient {

    @GetMapping("/notifications/api/notifications/{login}")
    List<String> fetchAll(@PathVariable("login") String login);
}
