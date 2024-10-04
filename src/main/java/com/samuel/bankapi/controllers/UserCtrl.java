package com.samuel.bankapi.controllers;

import com.samuel.bankapi.UserException;
import com.samuel.bankapi.models.dto.LoginDto;
import com.samuel.bankapi.models.entities.User;
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
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {

        System.out.println("user: " + user);
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            System.out.println("user: " + user);
            LoginDto loggedInUser = userService.loginUser(user);
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        catch (UserException.UserNotFoundException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (!userService.isExists(id)) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }
        catch (UserException.UserNotFoundException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}




