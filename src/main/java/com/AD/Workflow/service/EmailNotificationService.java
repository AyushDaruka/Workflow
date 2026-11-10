package com.AD.Workflow.service;

import com.AD.Workflow.exception.WorkflowNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
//        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your_email@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
             throw new WorkflowNotFoundException("Testing custom exception handling for email service");
//        }servic
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listenNotificationTopic(String message) {
        // For demonstration, we will just print the message and send an email
        System.out.println("Received message from Kafka: " + message);
        // In a real application, you would parse the message and extract email details
        // For this example, we will use hardcoded email details
    }
}
