package com.samuel.bankapi.models.dto;

import com.samuel.bankapi.models.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReadDto {
    private String id;
    private UserDto sender;
    private UserDto reciever;
    private double amount;
    private String transactionType;
    private String description;
    private String transactionDate;
    private String status;
    private String reference;
    private String transactionPin;
}
