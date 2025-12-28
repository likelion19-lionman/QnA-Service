package com.example.springqnaapp.config;

import com.example.springqnaapp.config.properties.MailProperties;
import com.example.springqnaapp.config.properties.MailSmtpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ MailProperties.class, MailSmtpProperties.class })
public class MailConfig {

    private final MailProperties mailProperties;
	private final MailSmtpProperties mailSmtpProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.host());
        mailSender.setPort(mailProperties.port());
        mailSender.setUsername(mailProperties.username());
        mailSender.setPassword(mailProperties.password());
        mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(mailSmtpProperties.toJavaMailProperties());
        return mailSender;
    }
}
