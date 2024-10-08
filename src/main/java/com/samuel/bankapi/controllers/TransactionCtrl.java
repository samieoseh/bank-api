package com.samuel.bankapi.controllers;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.TransactionDto;
import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.dto.VerifyUserDto;
import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.services.TransactionService;
import com.samuel.bankapi.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionCtrl {
    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    Mapper<TransactionEntity, TransactionDto> transactionMapper;

    @Autowired
    Mapper<UserEntity, VerifyUserDto> userMapper;

    @GetMapping("/verify-and-get-user/{id}")
    public ResponseEntity<VerifyUserDto> verifyAndGetUser(@PathVariable String id) {
        if (userService.isExists(id)) {
            UserEntity userEntity = userService.getUser(id);
            VerifyUserDto verifyUserDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(verifyUserDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/verify-balance/{amount}")
    public ResponseEntity<?> verifyBalance(@PathVariable double amount) {
        if (transactionService.isBalanceSufficient(amount)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Insufficient Funds", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDto transactionDto) {
        try {
            TransactionEntity transactionEntity = transactionMapper.mapFrom(transactionDto);
            TransactionEntity createdTransactionEntity = transactionService.createTransaction(transactionEntity);
            TransactionDto createdTransactionDto = transactionMapper.mapTo(createdTransactionEntity);
            return new ResponseEntity<>(createdTransactionDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
