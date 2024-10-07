package com.samuel.bankapi.mappers.impl;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.AccountTypeDto;
import com.samuel.bankapi.models.entities.AccountTypeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountTypeMapperImpl implements Mapper<AccountTypeEntity, AccountTypeDto> {
    private final ModelMapper modelMapper;

    public AccountTypeMapperImpl() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public AccountTypeDto mapTo(AccountTypeEntity accountTypeEntity) {
        return modelMapper.map(accountTypeEntity, AccountTypeDto.class);
    }

    @Override
    public AccountTypeEntity mapFrom(AccountTypeDto accountTypeDto) {
        return modelMapper.map(accountTypeDto, AccountTypeEntity.class);
    }
}
