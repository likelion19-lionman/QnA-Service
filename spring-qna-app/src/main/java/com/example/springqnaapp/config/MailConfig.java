package com.example.springqnaapp.config;

import com.example.springqnaapp.config.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        MailProperties.Smtp smtp = mailProperties.getSmtp();

        properties.put("mail.smtp.auth", smtp.isAuth());
        properties.put("mail.smtp.starttls.enable", smtp.getStarttls().isEnable());
        properties.put("mail.smtp.starttls.required", smtp.getStarttls().isRequired());
        properties.put("mail.smtp.connectiontimeout", smtp.getConnectionTimeout());
        properties.put("mail.smtp.timeout", smtp.getTimeout());
        properties.put("mail.smtp.writetimeout", smtp.getWriteTimeout());

        return properties;
    }
}
