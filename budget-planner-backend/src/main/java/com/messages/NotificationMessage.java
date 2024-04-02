package com.messages;

import com.model.enumerations.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {

    private Long userId;

    private NotificationType notificationType;

    private String month;
}
