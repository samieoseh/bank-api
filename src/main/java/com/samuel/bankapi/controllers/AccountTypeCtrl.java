package com.samuel.bankapi.controllers;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.AccountTypeDto;
import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.services.AccountTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-types")
public class AccountTypeCtrl {

    private final AccountTypeService accountTypeService;
    private final Mapper<AccountTypeEntity, AccountTypeDto> accountTypeDtoMapper;

    public AccountTypeCtrl(AccountTypeService accountTypeService, Mapper<AccountTypeEntity, AccountTypeDto> accountTypeDtoMapper) {
        this.accountTypeService = accountTypeService;
        this.accountTypeDtoMapper = accountTypeDtoMapper;
    }
    @GetMapping("")
    public ResponseEntity<List<AccountTypeDto>> getAccountTypes() {
        List<AccountTypeEntity> accountTypeEntities = accountTypeService.getAccountTypes();
        List<AccountTypeDto> accountTypeDtos = accountTypeEntities.stream().map(accountTypeDtoMapper::mapTo).toList();

        return new ResponseEntity<>(accountTypeDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountTypeDto> getAccountType(@PathVariable String id) {
        AccountTypeEntity accountTypeEntity = accountTypeService.getAccountType(id);
        if (accountTypeEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AccountTypeDto accountTypeDto = accountTypeDtoMapper.mapTo(accountTypeEntity);
        return new ResponseEntity<>(accountTypeDto, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<AccountTypeDto> createAccountType(@RequestBody AccountTypeDto accountTypeDto) {
        AccountTypeEntity accountTypeEntity = accountTypeDtoMapper.mapFrom(accountTypeDto);
        AccountTypeEntity createdAccountTypeEntity = accountTypeService.createAccountType(accountTypeEntity);
        AccountTypeDto createdAccountTypeDto = accountTypeDtoMapper.mapTo(createdAccountTypeEntity);
        return new ResponseEntity<>(createdAccountTypeDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountTypeDto> updateAccountType(@PathVariable String id, @RequestBody AccountTypeDto accountTypeDto) {
            if(accountTypeService.isExists(id)) {
                AccountTypeEntity accountTypeEntity = accountTypeDtoMapper.mapFrom(accountTypeDto);
                AccountTypeEntity updatedAccountTypeEntity = accountTypeService.updateAccountType(id, accountTypeEntity);
                AccountTypeDto updatedAccountTypeDto = accountTypeDtoMapper.mapTo(updatedAccountTypeEntity);
                return new ResponseEntity<>(updatedAccountTypeDto, HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountType(@PathVariable String id) {
        if(accountTypeService.isExists(id)) {
            accountTypeService.deleteAccountType(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // this is the correct status code for delete
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
