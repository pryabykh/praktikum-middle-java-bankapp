package com.pryabykh.bankapp.accounts.service;

import com.pryabykh.bankapp.accounts.entity.NotificationsOutbox;
import com.pryabykh.bankapp.accounts.repository.NotificationsOutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationsOutboxService {

    private final NotificationsOutboxRepository notificationsOutboxRepository;

    public NotificationsOutboxService(NotificationsOutboxRepository notificationsOutboxRepository) {
        this.notificationsOutboxRepository = notificationsOutboxRepository;
    }


    @Transactional
    public void createNotification(String login, String message) {
        NotificationsOutbox notification = new NotificationsOutbox();
        notification.setLogin(login);
        notification.setMessage(message);
        notificationsOutboxRepository.save(notification);
    }
}
