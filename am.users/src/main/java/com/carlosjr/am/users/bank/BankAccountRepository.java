package com.carlosjr.am.users.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    BankAccount findByAccountNumber(Long accountNumber);

}
