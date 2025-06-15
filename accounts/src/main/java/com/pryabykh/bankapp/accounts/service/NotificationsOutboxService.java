package com.pryabykh.bankapp.accounts.service;

import com.pryabykh.bankapp.accounts.entity.NotificationsOutbox;
import com.pryabykh.bankapp.accounts.feign.notifications.NotificationCreateDto;
import com.pryabykh.bankapp.accounts.feign.notifications.NotificationsFeignClient;
import com.pryabykh.bankapp.accounts.repository.NotificationsOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationsOutboxService {

    private static final Logger log = LoggerFactory.getLogger(NotificationsOutboxService.class);
    private final NotificationsOutboxRepository notificationsOutboxRepository;
    private final NotificationsFeignClient notificationsFeignClient;

    public NotificationsOutboxService(NotificationsOutboxRepository notificationsOutboxRepository, NotificationsFeignClient notificationsFeignClient) {
        this.notificationsOutboxRepository = notificationsOutboxRepository;
        this.notificationsFeignClient = notificationsFeignClient;
    }


    @Transactional
    public void createNotification(String login, String message) {
        NotificationsOutbox notification = new NotificationsOutbox();
        notification.setLogin(login);
        notification.setMessage(message);
        notificationsOutboxRepository.save(notification);
    }

    @Transactional
    @Scheduled(fixedDelay = 1000)
    public void sendNotification() {
        try {
            notificationsOutboxRepository.findAllByReceivedFalse().forEach(notification -> {
                NotificationCreateDto dto = new NotificationCreateDto();
                dto.setSourceId(notification.getId());
                dto.setLogin(notification.getLogin());
                dto.setMessage(notification.getMessage());
                notificationsFeignClient.create(dto);
                notification.setReceived(true);
                notificationsOutboxRepository.save(notification);
            });
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }
}
