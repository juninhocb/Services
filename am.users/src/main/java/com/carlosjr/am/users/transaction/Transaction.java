package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

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
    @JoinColumn(name = "bank_id", referencedColumnName = "id", columnDefinition = "VARCHAR(36)")
    private BankAccount bankAccount;
    @Column(nullable = false)
    private BigDecimal amount;
    @JdbcType(CharJdbcType.class)
    @Column(name = "invoice_id", unique = true, columnDefinition = "VARCHAR(36)")
    private UUID invoiceId;
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private Timestamp createdTime;

}
