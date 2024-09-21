package com.samuel.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_types")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String typeName; // e.g Checking, Savings, Loan etc

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Double minimumBalance;
}
