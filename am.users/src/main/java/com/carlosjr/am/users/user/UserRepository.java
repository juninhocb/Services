package com.carlosjr.am.users.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE current_access_token IS NOT NULL", nativeQuery = true)
    List<User> listOfLoggedUsers();

}
