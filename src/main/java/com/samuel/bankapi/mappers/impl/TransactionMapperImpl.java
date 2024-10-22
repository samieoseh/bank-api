package com.samuel.bankapi.mappers.impl;


import com.samuel.bankapi.mappers.Mapper;
import com.samuel.bankapi.models.dto.TransactionDto;
import com.samuel.bankapi.models.entities.TransactionEntity;
import com.samuel.bankapi.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component("transactionMapper")
public class TransactionMapperImpl implements Mapper<TransactionEntity, TransactionDto> {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public TransactionMapperImpl(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public TransactionDto mapTo(TransactionEntity transactionEntity) {
        TransactionDto dto =  modelMapper.map(transactionEntity, TransactionDto.class);

        dto.setSender(transactionEntity.getSender().getId());
        dto.setReciever(transactionEntity.getReciever().getId());

        return dto;
    }

    @Override
    public TransactionEntity mapFrom(TransactionDto transactionDto) {
        System.out.println("Sender: " + transactionDto.getSender());
        System.out.println("Reciever: " + transactionDto.getReciever());

        TransactionEntity entity = modelMapper.map(transactionDto, TransactionEntity.class);

        System.out.println("entity: " + entity);

        entity.setSender(userService.getUserById(transactionDto.getSender()));
        entity.setReciever(userService.getUserById(transactionDto.getReciever()));

        return entity;
    }
}
