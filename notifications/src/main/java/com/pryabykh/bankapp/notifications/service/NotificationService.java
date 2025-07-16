package com.pryabykh.bankapp.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.bankapp.notifications.dto.NotificationCreateDto;
import com.pryabykh.bankapp.notifications.entity.Notification;
import com.pryabykh.bankapp.notifications.repository.NotificationsRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationsRepository notificationsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationService(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @Transactional
    @RetryableTopic(
            // Определяем, сколько раз мы будем пытаться обработать сообщение
            attempts = "5",
            // Добавим экспоненциальную задержку, чтобы дать внешним системам время восстановиться
            backoff = @Backoff(delay = 1_00, multiplier = 2, maxDelay = 8_000),
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "notifications", containerFactory = "customKafkaListenerContainerFactory")
    public void create(String createDto) {
        try {
            NotificationCreateDto notification = objectMapper.readValue(createDto, NotificationCreateDto.class);
            notificationsRepository.insertOnConflictDoNothing(new Notification(
                    notification.getSourceId(), notification.getLogin(), notification.getMessage()
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<String> fetchAll(String login) {
        PageRequest page = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"));
        return notificationsRepository.findAllByLogin(login, page).map(Notification::getMessage).toList();
    }

    @DltHandler
    public void handleDltMessage(ConsumerRecord<?, ?> record) {
        System.out.println("Message landed in DLT: " + record.value());
    }
}
