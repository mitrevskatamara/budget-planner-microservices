package com.service.impl;

import com.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setText(content);
        emailSender.send(message);
    }
}