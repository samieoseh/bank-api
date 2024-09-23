package com.samuel.bankapi.models;

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
    private String accessToken;
}

