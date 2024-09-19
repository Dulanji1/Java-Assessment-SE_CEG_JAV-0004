package com.example.scheduling.system.controller;

import com.example.scheduling.system.dto.MailDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/sendemail")
    public ResponseEntity<String> sendEmail(@RequestBody MailDetailsDTO mailDetailsDTO) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(mailDetailsDTO.getSubject());
            message.setTo(mailDetailsDTO.getToMail());
            message.setFrom("dulanjicooray97@gmail.com");
            message.setText(mailDetailsDTO.getMessage());

            javaMailSender.send(message);

            logger.info("Email sent successfully to {}", mailDetailsDTO.getToMail());

            return ResponseEntity.ok("Successfully sent email!");

        } catch (Exception e) {
            logger.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}
