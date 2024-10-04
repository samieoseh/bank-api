package com.samuel.bankapi.controllers;

import com.samuel.bankapi.models.dto.EmailDto;
import com.samuel.bankapi.models.dto.VerifyDto;
import com.samuel.bankapi.services.EmailService;
import com.samuel.bankapi.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailCtrl {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto emailDto) {
        try {
            emailService.sendEmail(emailDto);
            return new ResponseEntity<>("Email sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Email not sent", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody VerifyDto verifyDto) {
        try {
            Boolean isEmailVerified = tokenService.verifyToken(verifyDto);
            System.out.println("isEmailVerified: " + isEmailVerified);

            if (!isEmailVerified) {
                return new ResponseEntity<>("Email not verified", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Email verified", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Email not verified", HttpStatus.BAD_REQUEST);
        }

    }
}
