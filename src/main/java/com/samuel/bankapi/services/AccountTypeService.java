package com.samuel.bankapi.services;

import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.repositories.AccountTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AccountTypeService {

    @Autowired
    AccountTypeRepo accountTypeRepo;

    public List<AccountTypeEntity> getAccountTypes() {
        return StreamSupport.stream(accountTypeRepo.findAll().spliterator(), false).toList();
    }

    public AccountTypeEntity createAccountType(AccountTypeEntity accountTypeEntity) {
        return accountTypeRepo.save(accountTypeEntity);
    }

    public AccountTypeEntity updateAccountType(String id, AccountTypeEntity accountTypeEntity) {
        accountTypeEntity.setId(id);
        return accountTypeRepo.findById(id).map(
                existingAccountType -> {
                    Optional.ofNullable(accountTypeEntity.getTypeName()).ifPresent(existingAccountType::setTypeName);
                    Optional.ofNullable(accountTypeEntity.getDescription()).ifPresent(existingAccountType::setDescription);
                    Optional.ofNullable(accountTypeEntity.getInterestRate()).ifPresent(existingAccountType::setInterestRate);
                    Optional.ofNullable(accountTypeEntity.getMinimumBalance()).ifPresent(existingAccountType::setMinimumBalance);
                    return accountTypeRepo.save(existingAccountType);
                }
        ).orElseThrow(() -> new RuntimeException("Account type not found"));
    }

    public void deleteAccountType(String id) {
        accountTypeRepo.deleteById(id);
    }

    public boolean isExists(String id) {
        return accountTypeRepo.existsById(id);
    }
}
