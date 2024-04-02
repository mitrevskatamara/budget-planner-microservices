package org.notificationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.notificationservice.model.enumerations.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime date = LocalDateTime.now();

    private String content;

    private Boolean read = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"email", "firstName", "lastName", "password", "status", "roles", "hibernateLazyInitializer"})
    private User user;

    private NotificationType notificationType;

}
