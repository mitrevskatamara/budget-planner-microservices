package org.notificationservice.controller;

import lombok.AllArgsConstructor;
import org.notificationservice.model.Notification;
import org.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/getAllNotifications")
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam Long userId) {
        return new ResponseEntity<>(notificationService.getAllNotifications(userId), HttpStatus.OK);
    }

    @GetMapping("/getPaginatedNotifications")
    public ResponseEntity<List<Notification>> getPaginatedNotifications(@RequestParam int page, @RequestParam int pageSize,
                                                                        @RequestParam Long userId) {
        return new ResponseEntity<>(notificationService.getPaginatedNotifications(page, pageSize, userId), HttpStatus.OK);
    }

    @PostMapping("/markAsRead")
    public void markAsReadNotification(@RequestParam String id) {
        notificationService.markAsReadNotification(Long.valueOf(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Long> deleteNotification(@RequestParam Long id) {
        Long deletedId = this.notificationService.deleteNotification(id);
        return new ResponseEntity<>(deletedId, HttpStatus.OK);
    }

}
