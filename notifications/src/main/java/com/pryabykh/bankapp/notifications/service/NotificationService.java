package com.pryabykh.bankapp.notifications.service;

import com.pryabykh.bankapp.notifications.dto.NotificationCreateDto;
import com.pryabykh.bankapp.notifications.entity.Notification;
import com.pryabykh.bankapp.notifications.repository.NotificationsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationsRepository notificationsRepository;

    public NotificationService(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @Transactional
    public void create(NotificationCreateDto createDto) {
        notificationsRepository.insertOnConflictDoNothing(new Notification(
                createDto.getSourceId(), createDto.getLogin(), createDto.getMessage()
        ));
    }

    @Transactional(readOnly = true)
    public List<String> fetchAll(String login) {
        PageRequest page = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"));
        return notificationsRepository.findAllByLogin(login, page).map(Notification::getMessage).toList();
    }
}
