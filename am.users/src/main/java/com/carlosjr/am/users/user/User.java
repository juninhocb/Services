package com.carlosjr.am.users.user;

import com.carlosjr.am.users.bank.BankAccount;
import com.carlosjr.am.users.roles.Roles;
import com.carlosjr.am.users.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Transaction> transactions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles;
    private Boolean active;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp updatedDate;
    private String currentAccessToken;

}
