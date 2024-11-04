package com.samuel.bankapi.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.TransactionReceiptDto;
import com.samuel.bankapi.models.entities.TransactionReceiptEntity;

@Component
public class TransactionReceiptReadMapperImpl implements Mapper<TransactionReceiptEntity, TransactionReceiptDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TransactionReceiptDto mapTo(TransactionReceiptEntity transactionReceiptEntity) {
        TransactionReceiptDto transactionReceiptDto = modelMapper.map(transactionReceiptEntity,
                TransactionReceiptDto.class);

        return transactionReceiptDto;
    }

    @Override
    public TransactionReceiptEntity mapFrom(TransactionReceiptDto transactionReceiptDto) {
        TransactionReceiptEntity transactionReceiptEntity = modelMapper.map(
                transactionReceiptDto, TransactionReceiptEntity.class);

        return transactionReceiptEntity;
    }
}
