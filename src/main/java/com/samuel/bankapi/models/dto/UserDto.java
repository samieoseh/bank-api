package com.samuel.bankapi.models.dto;

import com.samuel.bankapi.models.entities.AccountTypeEntity;
import com.samuel.bankapi.models.entities.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String fullName;
    private  String email;
    private String phoneNumber;
    private String accountNumber;
    private String address;
    private RoleEntity userRole;
    private AccountTypeEntity accountType;
    private BigDecimal balance;
    private boolean active;
    private String profilePictureUrl;
    private boolean emailVerified;
    private boolean phoneNumberVerified;
    private Date accountLockedUntil;
    private Date createdAt;
    private Date lastLoginAt;
}
