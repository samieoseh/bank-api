package com.samuel.bankapi.models.entities;

import com.samuel.bankapi.enums.StatusType;
import com.samuel.bankapi.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reciever_id", nullable = false)
    private UserEntity reciever;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date transactionDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status;

    private String reference;

    @Transient
    private String transactionPin;


}
