package com.samuel.bankapi.services;

import com.samuel.bankapi.models.dto.EmailDto;
import com.samuel.bankapi.models.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    public void sendToken(EmailDto emailDto) {
        emailService.sendEmail(emailDto);
    }

    public UserEntity createUser(UserEntity userEntity) {
        System.out.println(userEntity);
        return userService.registerUser(userEntity);
    }
}
