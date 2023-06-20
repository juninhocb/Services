package com.carlosjr.am.users.user;

import com.carlosjr.am.users.roles.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @ManyToMany(mappedBy = "users")
    private Set<Roles> roles;
    private Boolean active;
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp updatedDate;

}
