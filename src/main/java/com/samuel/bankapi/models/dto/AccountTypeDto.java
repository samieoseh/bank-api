package com.samuel.bankapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTypeDto {
    private String id;
    private String typeName;
    private String description;
    private Double interestRate;
    private Double minimumBalance;
}
