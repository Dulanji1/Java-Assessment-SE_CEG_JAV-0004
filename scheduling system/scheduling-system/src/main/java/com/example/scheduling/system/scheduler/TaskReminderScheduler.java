package com.example.scheduling.system.scheduler;

import com.example.scheduling.system.entity.Task;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.entity.EmailLog;
import com.example.scheduling.system.repository.TaskRepository;
import com.example.scheduling.system.repository.UserRepository;
import com.example.scheduling.system.repository.EmailLogRepository;
import com.example.scheduling.system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TaskReminderScheduler.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailLogRepository emailLogRepository;


    // Scheduled task to remind users of their tasks every minute
    // @Scheduled(cron = "0 * * * * ?")  // Cron expression for every minute

    // Scheduled task to remind users of their tasks every day at 8 PM//
    @Scheduled(cron = "0 0 20 * * ?")  // Cron expression for 8 PM daily
    public void remindUsersOfTasks() {
        logger.info("Running scheduled task: remindUsersOfTasks at {}", LocalDateTime.now());

        List<User> users = userRepository.findAll();
        for (User user : users) {
            remindPendingTasks(user);
            remindCompletedTasks(user);
            remindFailedTasks(user);
        }
    }

    private void remindPendingTasks(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "PENDING");
        for (Task task : tasks) {
            if (!taskIsAlreadyReminded(task)) {
                String subject = "Task Due Reminder: " + task.getName();
                String message = "Reminder: The task '" + task.getName() + "' is due!";
                sendEmailAsync(user.getEmail(), subject, message, user, task, "PENDING");
            }
        }
    }

    private void remindCompletedTasks(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "COMPLETED");
        for (Task task : tasks) {
            String subject = "Task Completed Reminder: " + task.getName();
            String message = "Congratulations! The task '" + task.getName() + "' has been completed.";
            sendEmailAsync(user.getEmail(), subject, message, user, task, "COMPLETED");
        }
    }

    private void remindFailedTasks(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "FAILED");
        for (Task task : tasks) {
            String subject = "Task Failed Reminder: " + task.getName();
            String message = "The task '" + task.getName() + "' has failed. Please check.";
            sendEmailAsync(user.getEmail(), subject, message, user, task, "FAILED");
        }
    }

    private void sendEmailAsync(String email, String subject, String message, User user, Task task, String taskStatus) {
        logger.info("Preparing to send email to '{}', Subject: '{}'", email, subject);
        boolean isEmailSent = emailService.sendEmail(email, subject, message);
        logger.info("Email sent status: {}", isEmailSent);
        logEmailStatus(user, task, taskStatus, isEmailSent);
    }

    private boolean taskIsAlreadyReminded(Task task) {
        List<EmailLog> emailLogs = emailLogRepository.findByTaskId(task.getId());
        return !emailLogs.isEmpty();
    }

    private void logEmailStatus(User user, Task task, String taskStatus, boolean emailSent) {
        EmailLog emailLog = new EmailLog();
        emailLog.setUserId(user.getId());
        if (task != null) {
            emailLog.setTaskId(task.getId());
        }
        emailLog.setTaskStatus(taskStatus);
        emailLog.setEmailSent(emailSent);
        emailLog.setTimestamp(LocalDateTime.now());

        logger.info("Logging email status for task ID: {}, User ID: {}, Status: {}, Email Sent: {}",
                emailLog.getTaskId(), emailLog.getUserId(), taskStatus, emailSent);

        emailLogRepository.save(emailLog);
    }
}
