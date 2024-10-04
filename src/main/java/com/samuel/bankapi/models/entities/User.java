package com.samuel.bankapi.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String transactionPin;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String fullName;

    private String address;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Double balance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_role_id", nullable = false)
    private Role userRole;

    @Column(nullable = false)
    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String profilePictureUrl;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private boolean phoneNumberVerified;

    @Column(nullable = false)
    private boolean twoFactorAuthEnabled;

    @Column(nullable = false)
    private int failedLoginAttempts;

    @Temporal(TemporalType.TIMESTAMP)
    private Date accountLockedUntil;

}
