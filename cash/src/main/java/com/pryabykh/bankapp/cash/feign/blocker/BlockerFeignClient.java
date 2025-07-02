package com.pryabykh.bankapp.cash.feign.blocker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "blocker", url = "${feign.blocker}")
public interface BlockerFeignClient {

    @PostMapping("/api/blocker/is-suspicious")
    boolean isSuspicious();
}
