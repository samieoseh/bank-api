package com.samuel.bankapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.models.entities.TransactionReceiptEntity;
import com.samuel.bankapi.models.entities.UserEntity;

@Repository
public interface TransactionReceiptRepo extends CrudRepository<TransactionReceiptEntity, String> {

    TransactionReceiptEntity findByTransactionAndUser(TransactionEntity transaction, UserEntity user);

}
