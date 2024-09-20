package com.example.scheduling.system.service;

import com.example.scheduling.system.entity.EmailLog;
import com.example.scheduling.system.entity.Task;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.EmailLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service // Marking this class as a Spring service
public class TaskService {

    private final EmailService emailService;
    private final EmailLogRepository emailLogRepository;

    @Autowired
    public TaskService(EmailService emailService, EmailLogRepository emailLogRepository) {
        this.emailService = emailService;
        this.emailLogRepository = emailLogRepository;
    }

    @Transactional // Ensuring the email sending and logging is part of the same transaction
    public String sendTaskCreationEmail(User user, Task task) {
        String subject = "New Task Created";
        String message = "A new task '" + task.getName() + "' has been created, scheduled for " + task.getScheduledTime() + ".";

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return "No email available for user. Email not sent.";
        }

        boolean isEmailSent = emailService.sendEmail(user.getEmail(), subject, message);

        // Log the email status
        EmailLog emailLog = new EmailLog();
        emailLog.setUserId(user.getId());
        emailLog.setTaskId(task.getId());
        emailLog.setTaskStatus("TASK_CREATED");
        emailLog.setEmailSent(isEmailSent);
        emailLog.setTimestamp(LocalDateTime.now());

        emailLogRepository.save(emailLog);
        return isEmailSent ? "Email sent successfully." : "Email failed to send.";
    }

    @Transactional
    public String sendTaskUpdateEmail(User user, Task task) {
        String subject = "Task Updated";
        String message = "The task '" + task.getName() + "' has been updated. New scheduled time: " + task.getScheduledTime() + ".";

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return "No email available for user. Email not sent.";
        }

        boolean isEmailSent = emailService.sendEmail(user.getEmail(), subject, message);

        // Log the email status
        EmailLog emailLog = new EmailLog();
        emailLog.setUserId(user.getId());
        emailLog.setTaskId(task.getId());
        emailLog.setTaskStatus("TASK_UPDATED");
        emailLog.setEmailSent(isEmailSent);
        emailLog.setTimestamp(LocalDateTime.now());

        emailLogRepository.save(emailLog);
        return isEmailSent ? "Email sent successfully." : "Email failed to send.";
    }
}
