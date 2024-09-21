package com.samuel.bankapi.controllers;

import com.samuel.bankapi.models.AccountType;
import com.samuel.bankapi.services.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-types")
public class AccountTypeCtrl {
    @Autowired
    AccountTypeService accountTypeService;

    @GetMapping("")
    public ResponseEntity<List<AccountType>> getAccountTypes() {
        List<AccountType> accountTypes = accountTypeService.getAccountTypes();
        return new ResponseEntity<>(accountTypes, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<AccountType> createAccountType(@RequestBody AccountType accountType) {
        return new ResponseEntity<>(accountTypeService.createAccountType(accountType), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountType> updateAccountType(@PathVariable String id, @RequestBody AccountType accountType) {
            if(accountTypeService.isExists(id)) {
                AccountType updatedAccountType = accountTypeService.updateAccountType(id, accountType);
                return new ResponseEntity<>(updatedAccountType, HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountType(@PathVariable String id) {
        if(accountTypeService.isExists(id)) {
            accountTypeService.deleteAccountType(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
