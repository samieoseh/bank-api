package com.samuel.bankapi.controllers;

import com.samuel.bankapi.models.LoginDto;
import com.samuel.bankapi.models.User;
import com.samuel.bankapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserCtrl {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        System.out.println("Getting users");
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        System.out.println("User: " + user);
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            System.out.println("User: " + user);
            LoginDto loggedInUser = userService.loginUser(user);
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}




