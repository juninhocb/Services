package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    BankAccount findByAccountNumber(Long accountNumber);
    List<BankAccount> findAccountsByUser(User user, PageRequest pageRequest);

}
