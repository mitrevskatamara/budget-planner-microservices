package org.notificationservice.repository;

import org.notificationservice.model.Notification;
import org.notificationservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByDateDesc(User user);

    Page<Notification> findByUser(User user, Pageable pageable);
}
