package com.example.springqnaapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {
    String sendSimpleMessage(String sendEmail) throws MessagingException;
}
