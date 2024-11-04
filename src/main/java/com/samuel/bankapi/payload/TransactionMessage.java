package com.samuel.bankapi.payload;

import java.util.Date;

import com.samuel.bankapi.models.dto.UserDto;
import com.samuel.bankapi.models.dto.VerifyUserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMessage {
    private String transactionType;
    private String transactionDescription;
    private Double transactionAmount;
    private Date transactionDate;
    private VerifyUserDto sender;
    private VerifyUserDto receiver;
}
