package com.example.StudySync;

import com.example.StudySync.reminder.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    EmailService emailService;
    @Test
    void testSendMail() {
        try {
            emailService.sendEmail("gaga85399@gmail.com", "Testing email using Spring Boot", "Hi, How are you?");
        } catch (Exception e) {
            fail("Email sending failed: " + e.getMessage());
        }
    }

}
