package com.samuel.bankapi.services;

import com.samuel.bankapi.models.entities.AccountType;
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

    public List<AccountType> getAccountTypes() {
        return StreamSupport.stream(accountTypeRepo.findAll().spliterator(), false).toList();
    }

    public AccountType createAccountType(AccountType accountType) {
        return accountTypeRepo.save(accountType);
    }

    public AccountType updateAccountType(String id, AccountType accountType) {
        accountType.setId(id);
        return accountTypeRepo.findById(id).map(
                existingAccountType -> {
                    Optional.ofNullable(accountType.getTypeName()).ifPresent(existingAccountType::setTypeName);
                    Optional.ofNullable(accountType.getDescription()).ifPresent(existingAccountType::setDescription);
                    Optional.ofNullable(accountType.getInterestRate()).ifPresent(existingAccountType::setInterestRate);
                    Optional.ofNullable(accountType.getMinimumBalance()).ifPresent(existingAccountType::setMinimumBalance);
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
