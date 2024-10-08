package com.samuel.bankapi.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "account_types")
public class AccountTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String typeName; // e.g Checking, Savings, Loan etc
    
    private String description;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Double minimumBalance;
}
