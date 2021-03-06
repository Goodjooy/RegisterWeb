package com.jacky.register.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.net.ssl.SSLSocketFactory;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.126.com");
        mailSender.setPort(465);

        mailSender.setUsername("imagestorage@126.com");
        mailSender.setPassword("SSTQDRCILQUPBDQB");

        mailSender.setDefaultEncoding("UTF-8");

        var props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", SSLSocketFactory.class);
        props.put("mail.smtp.socketFactory.fallback", false);

        return mailSender;
    }
}

