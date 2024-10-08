package com.samuel.bankapi.repositories;

import com.samuel.bankapi.models.entities.TransactionAuditEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionAuditRepo extends CrudRepository<TransactionAuditEntity, String> {

}
