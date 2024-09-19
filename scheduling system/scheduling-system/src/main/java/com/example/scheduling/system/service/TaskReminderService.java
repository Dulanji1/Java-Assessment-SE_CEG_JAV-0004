package com.example.scheduling.system.service;

import com.example.scheduling.system.entity.Task;
import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.entity.EmailLog;
import com.example.scheduling.system.repository.TaskRepository;
import com.example.scheduling.system.repository.UserRepository;
import com.example.scheduling.system.repository.EmailLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private EmailLogRepository emailLogRepository; // To log email status in DB

    @Scheduled(cron = "0 0 20 * * ?") // Every day at 8 PM
    public void remindDueTasks() {
        logger.info("Running scheduled task: remindDueTasks at {}", LocalDateTime.now());
        List<User> users = userRepository.findAll(); // Fetch all users
        for (User user : users) {
            remindPendingTasks(user);
            remindCompletedTasks(user);
            remindFailedTasks(user);
        }
    }

    private void remindPendingTasks(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "PENDING");
        for (Task task : tasks) {
            if (task.getScheduledTime().isBefore(LocalDateTime.now())) {
                boolean isEmailSent = emailService.sendEmail(user.getEmail(), "Task Due Reminder",
                        "Reminder: The task '" + task.getName() + "' is due!");
                logger.info("Sending reminder for task '{}', Email sent: {}", task.getName(), isEmailSent);
                logEmailStatus(user, task, "PENDING", isEmailSent);
            }
        }
    }

    private void remindCompletedTasks(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "COMPLETED");
        for (Task task : tasks) {
            if (task.getScheduledTime().isBefore(LocalDateTime.now())) {
                boolean isEmailSent = emailService.sendEmail(user.getEmail(), "Task Completed Reminder",
                        "Good job! The task '" + task.getName() + "' has been completed.");
                logger.info("Sending completed task reminder for '{}', Email sent: {}", task.getName(), isEmailSent);
                logEmailStatus(user, task, "COMPLETED", isEmailSent);
            }
        }
    }

    private void remindFailedTasks(User user) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(user.getId(), "FAILED");
        for (Task task : tasks) {
            if (task.getScheduledTime().isBefore(LocalDateTime.now())) {
                boolean isEmailSent = emailService.sendEmail(user.getEmail(), "Task Failed Reminder",
                        "Alert: The task '" + task.getName() + "' has failed.");
                logger.info("Sending failed task reminder for '{}', Email sent: {}", task.getName(), isEmailSent);
                logEmailStatus(user, task, "FAILED", isEmailSent);
            }
        }
    }

    @Scheduled(cron = "0 0 9 1 * ?") // First day of each month at 9 AM
    public void sendMonthlyMotivation() {
        logger.info("Running scheduled task: sendMonthlyMotivation at {}", LocalDateTime.now());
        String motivationalQuote = "Keep pushing forward! Every day is a new opportunity.";
        List<User> users = userRepository.findAll();
        for (User user : users) {
            boolean isEmailSent = emailService.sendEmail(user.getEmail(), "Monthly Motivation", motivationalQuote);
            logger.info("Sending motivational email to '{}', Email sent: {}", user.getEmail(), isEmailSent);
            logEmailStatus(user, null, "MOTIVATION", isEmailSent);
        }
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
        emailLogRepository.save(emailLog);
    }
}
