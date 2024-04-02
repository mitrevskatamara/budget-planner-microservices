package org.notificationservice.service;

import org.notificationservice.model.Notification;
import org.notificationservice.model.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    Notification createNotification(NotificationDto notificationDto);

    List<Notification> getAllNotifications(Long userId);

    List<Notification> getPaginatedNotifications(int page, int pageSize, Long userId);

    void markAsReadNotification(Long id);

    Long deleteNotification(Long id);
}
