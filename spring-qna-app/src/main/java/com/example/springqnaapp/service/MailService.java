package com.example.springqnaapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {

    String createCode();

    MimeMessage createMail(String mail, String authCode) throws MessagingException;

    String sendSimpleMessage(String sendEmail) throws MessagingException;
}
