package com.samuel.bankapi.services;

import com.samuel.bankapi.models.entities.TransactionAuditEntity;
import com.samuel.bankapi.repositories.TransactionAuditRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionAuditService {
    @Autowired
    TransactionAuditRepo transactionAuditRepo;

    public TransactionAuditEntity createTransactionAudit(TransactionAuditEntity transactionAuditEntity) {
        return transactionAuditRepo.save(transactionAuditEntity);
    }
}
