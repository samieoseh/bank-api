package com.samuel.bankapi.controllers;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.TransactionDto;
import com.samuel.bankapi.models.dto.TransactionReadDto;
import com.samuel.bankapi.models.dto.VerifyUserDto;
import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.payload.TransactionMessage;
import com.samuel.bankapi.producer.KafkaJsonProducer;
import com.samuel.bankapi.services.TransactionService;
import com.samuel.bankapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionCtrl {
    @Autowired
    TransactionService transactionService;

    @Autowired
    private KafkaJsonProducer kafkaJsonProducer;

    @Autowired
    UserService userService;

    @Autowired
    @Qualifier("transactionMapper")
    Mapper<TransactionEntity, TransactionDto> transactionMapper;

    @Autowired
    @Qualifier("transactionReadMapper")
    Mapper<TransactionEntity, TransactionReadDto> transactionReadMapper;

    @Autowired
    Mapper<UserEntity, VerifyUserDto> userMapper;


    @GetMapping("/verify-and-get-user/{accountNumber}")
    public ResponseEntity<VerifyUserDto> verifyAndGetUser(@PathVariable String accountNumber) {
        if (userService.isExists(accountNumber)) {
            System.out.println("User exists: " + accountNumber);
            UserEntity userEntity = userService.getUser(accountNumber);
            VerifyUserDto verifyUserDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(verifyUserDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/verify-balance/{amount}")
    public ResponseEntity<?> verifyBalance(@PathVariable double amount) {
        UserEntity userEntity = userService.getCurrentUser();
        if (transactionService.isBalanceSufficient(userEntity, amount)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("")
    // @Transactional
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDto transactionDto) {
        try {
            TransactionEntity transactionEntity = transactionMapper.mapFrom(transactionDto);
            TransactionEntity createdTransactionEntity = transactionService.createTransaction(transactionEntity);
            TransactionDto createdTransactionDto = transactionMapper.mapTo(createdTransactionEntity);
            TransactionMessage transactionMessage = new TransactionMessage();
            transactionMessage.setTransactionType(createdTransactionDto.getTransactionType());
            transactionMessage.setTransactionDescription(createdTransactionDto.getDescription());
            transactionMessage.setTransactionAmount(createdTransactionDto.getAmount());
            transactionMessage.setTransactionDate(createdTransactionEntity.getTransactionDate());
            UserEntity sender = userService.getUserById(createdTransactionDto.getSender());
            UserEntity receiver = userService.getUserById(createdTransactionDto.getReciever());
            VerifyUserDto senderDto = userMapper.mapTo(sender);
            VerifyUserDto receiverDto = userMapper.mapTo(receiver);
            transactionMessage.setSender(senderDto);
            transactionMessage.setReceiver(receiverDto);
            kafkaJsonProducer.sendMessage(transactionMessage);

            return new ResponseEntity<>(createdTransactionDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllTransactions() {
        try {
            List<TransactionEntity> transactionEntities = transactionService.getTransactions();
            List<TransactionReadDto> transactionDtos = transactionEntities.stream().map(transactionReadMapper::mapTo).toList();
            return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
