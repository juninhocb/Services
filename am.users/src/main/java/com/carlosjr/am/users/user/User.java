package com.carlosjr.am.users.user;

import com.carlosjr.am.users.roles.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, updatable = false, nullable = false)*/
    private Long id;
    @Column(name = "group_id")
    private Long groupId;
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
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp updatedDate;
    private String currentAccessToken;

}
