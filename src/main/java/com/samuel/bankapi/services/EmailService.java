package com.samuel.bankapi.services;

import com.samuel.bankapi.models.dto.EmailDto;
import com.samuel.bankapi.models.dto.VerifyDto;
import com.samuel.bankapi.models.entities.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private TokenService tokenService;

    private final  JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        // create token
        Token token = tokenService.createAndStoreToken(emailDto.getTo());
        System.out.println("token: " + token);

        String emailText = "Verify your email\n" + "Please use the code below to verify your email address\n" + token.getToken();

        mailMessage.setFrom("samueloseh007@gmail.com");
        mailMessage.setTo(emailDto.getTo());
        mailMessage.setSubject("Bank App email Verification");
        mailMessage.setText(emailText);

        javaMailSender.send(mailMessage);
    }

}
