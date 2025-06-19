package com.pryabykh.bankapp.cash.feign.blocker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "gateway", contextId = "blocker")
public interface BlockerFeignClient {

    @PostMapping("/blocker/api/blocker/is-suspicious")
    boolean isSuspicious();
}
