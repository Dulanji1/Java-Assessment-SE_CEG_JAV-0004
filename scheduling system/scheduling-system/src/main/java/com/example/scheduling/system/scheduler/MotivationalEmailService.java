package com.example.scheduling.system.scheduler;

import com.example.scheduling.system.entity.User;
import com.example.scheduling.system.repository.UserRepository;
import com.example.scheduling.system.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class MotivationalEmailService {

    private static final Logger logger = LoggerFactory.getLogger(MotivationalEmailService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // Sends motivational emails to all users
    @Async
    public void sendMotivationalEmails() {
        logger.info("Starting to send motivational emails");

        // Fetch all users from the database
        List<User> users = userRepository.findAll();
        String subject = "Stay Motivated!";
        String message = "Dear user, stay focused and keep pushing towards your goals!";

        // Send emails to all users
        for (User user : users) {
            sendMotivationalEmailToUser(user.getEmail(), subject, message);
        }

        logger.info("Finished sending motivational emails.");
    }

    // Sends an email to an individual user
    private void sendMotivationalEmailToUser(String email, String subject, String message) {
        logger.info("Sending email to {}", email);
        emailService.sendEmail(email, subject, message);
    }
}
