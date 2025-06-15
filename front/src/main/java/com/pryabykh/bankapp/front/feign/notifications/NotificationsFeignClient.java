package com.pryabykh.bankapp.front.feign.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "notifications", url = "${feign.notifications}")
public interface NotificationsFeignClient {

    @GetMapping("/api/notifications/{login}")
    List<String> fetchAll(@PathVariable("login") String login);
}
