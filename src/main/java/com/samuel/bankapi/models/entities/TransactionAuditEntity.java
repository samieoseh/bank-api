package com.samuel.bankapi.models.entities;

import com.samuel.bankapi.enums.ActionType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transaction_audits")
public class TransactionAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "transaction_id", nullable = false)
    @ManyToOne(cascade = CascadeType.MERGE)
    private TransactionEntity transaction;

    @Type(JsonType.class)
    @Column(name = "old_data", columnDefinition = "jsonb")
    private String oldData; // Stored as JSON string

    @Type(JsonType.class)
    @Column(name = "new_data", columnDefinition = "jsonb")
    private String newData; // Stored as JSON string

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;

    @JoinColumn(name = "performed_by", nullable = false)
    @ManyToOne(cascade = CascadeType.MERGE)
    private UserEntity performedBy;

    @Column(name = "performed_at", nullable = false)
    private Date performedAt;

}
