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
    BankAccountDto depositAmount(Long bankAccountNumber, BigDecimal amount);
    BankAccountDto withdrawAmount(Long bankAccountNumber, BigDecimal amount);
    long getRepositorySize();
    BankAccountDto findAccountByAccountNumber(Long accountNumber);
    BankAccount findPersistedByAccountNumber(Long accountNumber);
    Set<BankAccountDto> retrieveBankAccountsByUser(String email, PageRequest pageRequest);
}
