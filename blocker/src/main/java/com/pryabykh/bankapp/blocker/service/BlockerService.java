package com.pryabykh.bankapp.blocker.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BlockerService {
    private static final int CHANCE_OF_BLOCKING_OPERATION_PERCENT = 15;

    public boolean isSuspicious() {
        Random random = new Random();
        int roll = random.nextInt(100);
        return roll < CHANCE_OF_BLOCKING_OPERATION_PERCENT;
    }
}
