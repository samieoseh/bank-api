package com.samuel.bankapi.mappers.impl;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.TransactionDto;
import com.samuel.bankapi.models.dto.TransactionReadDto;
import com.samuel.bankapi.models.entities.TransactionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("transactionReadMapper")
public class TransactionReadMapperImpl implements Mapper<TransactionEntity, TransactionReadDto> {
    private final ModelMapper modelMapper;

    public TransactionReadMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionReadDto mapTo(TransactionEntity transactionEntity) {
        return modelMapper.map(transactionEntity, TransactionReadDto.class);
    }

    @Override
    public TransactionEntity mapFrom(TransactionReadDto transactionDto) {
        return modelMapper.map(transactionDto, TransactionEntity.class);
    }
}
