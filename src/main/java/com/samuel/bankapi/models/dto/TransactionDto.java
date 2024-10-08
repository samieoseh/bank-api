package com.samuel.bankapi.models.dto;

import com.samuel.bankapi.models.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String id;
    private String sender;
    private String reciever;
    private double amount;
    private String transactionType;
    private String description;
    private String transactionDate;
    private String status;
    private String reference;
    private String transactionPin;
}
