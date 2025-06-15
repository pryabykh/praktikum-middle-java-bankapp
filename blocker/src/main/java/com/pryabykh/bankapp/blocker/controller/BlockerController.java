package com.pryabykh.bankapp.blocker.controller;

import com.pryabykh.bankapp.blocker.service.BlockerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blocker")
public class BlockerController {

    private final BlockerService blockerService;

    public BlockerController(BlockerService blockerService) {
        this.blockerService = blockerService;
    }

    @PostMapping("/is-suspicious")
    public boolean isSuspicious() {
        return blockerService.isSuspicious();
    }
}
