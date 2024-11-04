package com.samuel.bankapi.services;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.TransactionReceiptEntity;
import com.samuel.bankapi.models.entities.UserEntity;
import com.samuel.bankapi.repositories.TransactionReceiptRepo;

@Service
public class TransactionReceiptService {

    @Autowired
    private TransactionReceiptRepo transactionReceiptRepo;

    public List<TransactionReceiptEntity> getReceipts() {
        return StreamSupport.stream(transactionReceiptRepo.findAll().spliterator(), false).toList();
    }

    public TransactionReceiptEntity getReceipt(TransactionEntity transaction, UserEntity user) {
        return transactionReceiptRepo.findByTransactionAndUser(transaction, user);
    }

}
