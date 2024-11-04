package com.samuel.bankapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.TransactionReceiptDto;
import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.TransactionReceiptEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.services.TransactionReceiptService;
import com.samuel.bankapi.services.TransactionService;
import com.samuel.bankapi.services.UserService;

@RestController
@RequestMapping("/api/transactions/receipts")
public class TransactionReceiptCtrl {
    @Autowired
    private TransactionReceiptService transactionReceiptService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<TransactionReceiptEntity, TransactionReceiptDto> transactionReceiptMapper;

    @GetMapping("")
    public ResponseEntity<?> getReceipts() {
        try {
            List<TransactionReceiptEntity> receipts = transactionReceiptService.getReceipts();
            List<TransactionReceiptDto> receiptDtos = receipts.stream().map(transactionReceiptMapper::mapTo).toList();
            return new ResponseEntity<>(receiptDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/{userId}")
    public ResponseEntity<?> getReceipt(@PathVariable String id, @PathVariable String userId) {
        try {
            TransactionEntity transaction = transactionService.getTransaction(id);
            if (transaction == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            UserEntity user = userService.getUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            TransactionReceiptEntity receipt = transactionReceiptService.getReceipt(transaction, user);
            TransactionReceiptDto receiptDto = transactionReceiptMapper.mapTo(receipt);
            return new ResponseEntity<>(receiptDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
