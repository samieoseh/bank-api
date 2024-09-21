package com.samuel.bankapi.controllers;

import com.samuel.bankapi.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCtrl {
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return user;
    }
}
