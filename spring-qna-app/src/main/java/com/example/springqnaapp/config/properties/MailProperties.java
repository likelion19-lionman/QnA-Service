package com.example.springqnaapp.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
public class MailProperties {

    private String host;
    private int port;
    private String username;
    private String password;

    private Smtp smtp = new Smtp();

    @Getter
    public static class Smtp {
        private boolean auth;
        private int connectionTimeout;
        private int timeout;
        private int writeTimeout;
        private Starttls starttls;
    }

    @Getter
    public static class Starttls {
        private boolean enable;
        private boolean required;
    }
}
