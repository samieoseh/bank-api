package com.samuel.bankapi.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    private String password;

    private String transactionPin;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String fullName;

    private String address;

    private String accountNumber;

    private Double balance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountTypeEntity accountType;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_role_id", nullable = false)
    private RoleEntity userRole;

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

    private int failedLoginAttempts;

    @Temporal(TemporalType.TIMESTAMP)
    private Date accountLockedUntil;

}
