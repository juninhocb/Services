package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "account_number", unique = true)
    private Long accountNumber;
    private String name;
    @Column(name = "is_active")
    private Boolean isActive;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name= "user_id", referencedColumnName = "id", columnDefinition = "VARCHAR(36)")
    private User user;
    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Timestamp updatedTime;


}
