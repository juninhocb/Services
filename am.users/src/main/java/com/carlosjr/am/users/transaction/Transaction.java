package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccount;
import com.carlosjr.am.users.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "VARCHAR(36)")
    private User user;
    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "id", columnDefinition = "VARCHAR(36)")
    private BankAccount bankAccount;
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(value = EnumType.STRING)
    private TransactionState state;
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private Timestamp createdTime;

}
