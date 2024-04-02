package org.notificationservice.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationMessageResponse {

    private String userId;

    private String notificationContent;
}
