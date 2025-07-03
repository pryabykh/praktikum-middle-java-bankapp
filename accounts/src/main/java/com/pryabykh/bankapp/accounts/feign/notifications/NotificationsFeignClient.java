package com.pryabykh.bankapp.accounts.feign.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "notifications", url = "${feign.notifications}")
public interface NotificationsFeignClient {

    @PostMapping("/api/notifications")
    void create(@RequestBody NotificationCreateDto createDto);
}
