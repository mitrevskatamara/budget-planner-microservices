package org.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.notificationservice.model.enumerations.NotificationType;


@Data
public class NotificationDto {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("notificationType")
    private NotificationType notificationType;

    @JsonProperty("month")
    private String month;

}
