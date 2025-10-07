package com.example.StudySync.reminder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject(subject);
            mail.setTo(to);
            mail.setText(body);
            javaMailSender.send(mail);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw new RuntimeException(e);
        }
    }



}
