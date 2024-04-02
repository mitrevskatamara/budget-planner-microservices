package org.notificationservice.service.impl;

import lombok.AllArgsConstructor;
import org.notificationservice.messages.NotificationMessageResponse;
import org.notificationservice.model.Notification;
import org.notificationservice.model.User;
import org.notificationservice.model.dto.NotificationDto;
import org.notificationservice.model.enumerations.NotificationType;
import org.notificationservice.repository.NotificationRepository;
import org.notificationservice.repository.UserRepository;
import org.notificationservice.service.NotificationService;
import org.notificationservice.service.RabbitMqService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final RabbitMqService rabbitMqService;

    private final static String LOW_BUDGET_CONTENT = "Oh no! Your budget for %s is running low and has gone below 0.";

    private final static String TRANSACTION_DELETE_CONTENT = "You have just deleted a transaction from your %s budget.";

    @Override
    public Notification createNotification(NotificationDto notificationDto) {
        Notification notification = new Notification();
        User user = userRepository.findById(notificationDto.getUserId()).orElse(null);

        notification.setUser(user);

        NotificationType notificationType = notificationDto.getNotificationType();
        if (notificationType.equals(NotificationType.LOW_BUDGET)) {
            notification.setContent(String.format(LOW_BUDGET_CONTENT, notificationDto.getMonth()));
            notification.setNotificationType(NotificationType.LOW_BUDGET);
        } else if (notificationType.equals(NotificationType.TRANSACTION_DELETE)) {
            notification.setContent(String.format(TRANSACTION_DELETE_CONTENT, notificationDto.getMonth()));
            notification.setNotificationType(NotificationType.TRANSACTION_DELETE);
        }

        NotificationMessageResponse messageResponse = new NotificationMessageResponse(notification.getUser().getId().toString(), notification.getContent());

        rabbitMqService.sendMessageToBudgetPlanner(messageResponse);

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return notificationRepository.findByUserOrderByDateDesc(user);
    }

    @Override
    public List<Notification> getPaginatedNotifications(int page, int pageSize, Long userId) {
        Sort sortByDateDesc = Sort.by(Sort.Order.desc("date"));
        Pageable pageable = PageRequest.of(page - 1, pageSize, sortByDateDesc);
        User user = this.userRepository.findById(userId).orElse(null);
        Page<Notification> notificationPage = notificationRepository.findByUser(user, pageable);
        return notificationPage.getContent();
    }

    @Override
    public void markAsReadNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);

        if (notification != null) {
            notification.setRead(!notification.getRead().equals(true));

            notificationRepository.save(notification);
        }

    }

    @Override
    public Long deleteNotification(Long id) {
        this.notificationRepository.deleteById(id);
        return id;
    }

}
