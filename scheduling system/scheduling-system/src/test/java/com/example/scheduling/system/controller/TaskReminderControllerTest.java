package com.example.scheduling.system.controller;

import com.example.scheduling.system.service.TaskReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;


public class TaskReminderControllerTest {

    @InjectMocks
    private TaskReminderController taskReminderController;

    @Mock
    private TaskReminderService taskReminderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotifications_Success() {
        ResponseEntity<String> response = taskReminderController.sendNotifications();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notifications sent successfully.", response.getBody());
    }

    @Test
    public void testSendNotifications_Failure() {
        // Simulate an exception being thrown by the service
        doThrow(new RuntimeException("Service error")).when(taskReminderService).sendImmediateNotifications();

        ResponseEntity<String> response = taskReminderController.sendNotifications();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to send notifications: Service error", response.getBody());
    }
}
