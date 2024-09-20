package com.example.scheduling.system.controller;

import com.example.scheduling.system.service.TaskReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskReminderController {

    @Autowired
    private TaskReminderService taskReminderService;

    @PostMapping("/api/notifications/send")
    public ResponseEntity<String> sendNotifications() {
        try {
            // Trigger the service to send notifications
            taskReminderService.sendImmediateNotifications();
            return ResponseEntity.status(HttpStatus.OK).body("Notifications sent successfully.");
        } catch (Exception e) {
            // Handle any exceptions and return a server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send notifications: " + e.getMessage());
        }
    }
}
