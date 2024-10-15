package com.samuel.bankapi.controllers;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.EmailDto;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminCtrl {
    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    private AdminService adminService;

    @PostMapping("/send-token")
    public ResponseEntity<?> sendToken(@RequestBody EmailDto emailDto) {
        try {
            adminService.sendToken(emailDto);
            return new ResponseEntity<>("Token sent to " + emailDto.getTo(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUserEntity = adminService.createUser(userEntity);
            UserDto savedUserDto = userMapper.mapTo(savedUserEntity);

            return new ResponseEntity<>(savedUserDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
