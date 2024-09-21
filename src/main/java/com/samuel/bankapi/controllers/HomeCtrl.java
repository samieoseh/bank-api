package com.samuel.bankapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeCtrl {
    @GetMapping("/")
    public String home()  {
        return "Hello World";
    }
}
