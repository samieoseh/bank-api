package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.entities.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends CrudRepository<TransactionEntity, String> {
    List<TransactionEntity> findAllByOrderByTransactionDateAsc();

    List<TransactionEntity> findAllByOrderByTransactionDateDesc();
}
