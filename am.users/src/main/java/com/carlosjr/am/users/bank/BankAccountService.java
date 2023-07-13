package com.carlosjr.am.users.bank;

import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public interface BankAccountService {
    UUID createNewBankAccount(BankAccountDto bankAccountDto);
    BankAccountDto findBankAccountById(UUID id);
    void updateBankAccount(UUID id, String name);
    void toggleBankAccount(UUID id);
    void depositAmount(UUID invoiceId, UUID bankId, BigDecimal amount);
    void withdrawAmount(UUID id, BigDecimal amount);
    long getRepositorySize();
    BankAccountDto findAccountByAccountNumber(Long accountNumber);
    BankAccount findPersistedByAccountNumber(Long accountNumber);
    Set<BankAccountDto> retrieveBankAccountsByUser(String email, PageRequest pageRequest);
}
