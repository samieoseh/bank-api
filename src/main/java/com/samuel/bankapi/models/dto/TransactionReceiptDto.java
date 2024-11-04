package com.samuel.bankapi.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReceiptDto {
    private String id;
    private TransactionReadDto transaction;
    private String transactionType;
    private Date transactionDate;
    private Double amount;
    private Double previousBalance;
    private Double newBalance;
    private UserDto user;
    private String description;
    private String status;
    private String reference;
}
