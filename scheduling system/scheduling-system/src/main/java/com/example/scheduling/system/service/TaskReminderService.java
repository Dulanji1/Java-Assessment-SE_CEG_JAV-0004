package com.example.scheduling.system.service;

import com.example.scheduling.system.entity.Task;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.entity.EmailLog;
import com.example.scheduling.system.repository.TaskRepository;
import com.example.scheduling.system.repository.UserRepository;
import com.example.scheduling.system.repository.EmailLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TaskReminderService {

    private static final Logger logger = LoggerFactory.getLogger(TaskReminderService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    // This API will send notifications immediately when called
    public void sendImmediateNotifications() {
        logger.info("Manual API call: Sending notifications for pending tasks.");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            sendPendingNotification(user);
            sendCompletedNotification(user);
            sendFailedNotification(user);
        }
    }


    private void sendPendingNotification(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "PENDING");
        for (Task task : tasks) {
            if (task.getScheduledTime().isBefore(LocalDateTime.now())) {
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    String subject = "Task Due Reminder";
                    String message = "Reminder: The task '" + task.getName() + "' is due!\n" +
                            "Scheduled Time: " + task.getScheduledTime() + "\n" +
                            "Status: PENDING";
                    boolean isEmailSent = emailService.sendEmail(user.getEmail(), subject, message);
                    logger.info("Sending reminder for task '{}', Email sent: {}", task.getName(), isEmailSent);
                    logEmailStatus(user, task, "PENDING", isEmailSent);

                } else {
                    logger.warn("User '{}' has no email address. Skipping task '{}'.", user.getId(), task.getName());
                }
            }
        }
    }

    private void sendCompletedNotification(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "COMPLETED");
        for (Task task : tasks) {
            if (task.getScheduledTime().isBefore(LocalDateTime.now()) && user.getEmail() != null && !user.getEmail().isEmpty()) {
                String subject = "Task Completed Reminder";
                String message = "Good job! The task '" + task.getName() + "' has been completed.\n" +
                        "Completion Time: " + task.getCompletionTime() + "\n" +
                        "Status: COMPLETED";
                boolean isEmailSent = emailService.sendEmail(user.getEmail(), subject, message);
                logger.info("Sending completed task reminder for '{}', Email sent: {}", task.getName(), isEmailSent);
                logEmailStatus(user, task, "COMPLETED", isEmailSent);
            }
        }
    }

    private void sendFailedNotification(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "FAILED");
        for (Task task : tasks) {
            if (task.getScheduledTime().isBefore(LocalDateTime.now()) && user.getEmail() != null && !user.getEmail().isEmpty()) {
                String subject = "Task Failed Reminder";
                String message = "Alert: The task '" + task.getName() + "' has failed.\n" +
                        "Scheduled Time: " + task.getScheduledTime() + "\n" +
                        "Status: FAILED";
                boolean isEmailSent = emailService.sendEmail(user.getEmail(), subject, message);
                logger.info("Sending failed task reminder for '{}', Email sent: {}", task.getName(), isEmailSent);
                logEmailStatus(user, task, "FAILED", isEmailSent);
            }
        }
    }


    // Logging the email status to database
    private void logEmailStatus(User user, Task task, String taskStatus, boolean emailSent) {
        EmailLog emailLog = new EmailLog();
        emailLog.setUserId(user.getId());
        if (task != null) {
            emailLog.setTaskId(task.getId());
        }
        emailLog.setTaskStatus(taskStatus);
        emailLog.setEmailSent(emailSent);
        emailLog.setTimestamp(LocalDateTime.now());
        emailLogRepository.save(emailLog);
    }
}
