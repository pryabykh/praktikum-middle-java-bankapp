package com.pryabykh.bankapp.notifications.repository;

import com.pryabykh.bankapp.notifications.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query(value = "INSERT INTO notifications.notifications(source_id, login, message) " +
            "VALUES (:#{#notification.sourceId}, :#{#notification.login}, :#{#notification.message}) " +
            "ON CONFLICT DO NOTHING",
            nativeQuery = true)
    void insertOnConflictDoNothing(Notification notification);

    Page<Notification> findAllByLogin(String login, Pageable pageable);
}
