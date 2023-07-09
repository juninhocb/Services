package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccount;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findTransactionByInvoiceId(UUID invoiceId);
    List<Transaction> findTransactionsByBankAccount(BankAccount bankAccount, PageRequest pageRequest);

}
