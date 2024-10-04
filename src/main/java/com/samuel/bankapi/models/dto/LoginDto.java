package com.samuel.bankapi.models.dto;

import com.samuel.bankapi.models.entities.AccountType;
import com.samuel.bankapi.models.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private Double balance;
    private AccountType accountType;
    private Role userRole;
    private String accountNumber;
    private String phoneNumber;
    private Boolean active;
    private String accessToken;
}

