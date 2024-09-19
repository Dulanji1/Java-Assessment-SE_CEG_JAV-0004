package com.example.scheduling.system.controller;

import com.example.scheduling.system.entity.EmailLog;
import com.example.scheduling.system.repository.EmailLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/email-logs")
public class EmailLogController {

    @Autowired
    private EmailLogRepository emailLogRepository;

    // Retrieve all email logs
    @GetMapping
    public List<EmailLog> getAllEmailLogs() {
        return emailLogRepository.findAll();
    }

    // Retrieve email logs by user ID
    @GetMapping("/by-user")
    public List<EmailLog> getEmailLogsByUserId(@RequestParam Long userId) {
        return emailLogRepository.findByUserId(userId);
    }

    // Retrieve email logs by task ID
    @GetMapping("/by-task")
    public List<EmailLog> getEmailLogsByTaskId(@RequestParam Long taskId) {
        return emailLogRepository.findByTaskId(taskId);
    }
}
