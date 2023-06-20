package com.carlosjr.am.users.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    @Query("SELECT r FROM Roles r WHERE r.isBasic = true")
    Set<Roles> findBasicRoles();

}
