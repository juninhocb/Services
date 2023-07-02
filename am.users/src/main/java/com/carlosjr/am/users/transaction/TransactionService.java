package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.user.UserDto;

import java.util.Set;
import java.util.UUID;

public interface TransactionService {
    UUID createNewTransaction(TransactionDto transactionDto);
    TransactionDto findTransactionById(UUID id);
    Set<TransactionDto> retrieveTransactionsByUser(UserDto userDto);
    Set<TransactionDto> retrieveTransactionsByGroupId(Long groupId);
    Set<TransactionDto> retrieveTransactionsByBankAccount(BankAccountDto bankAccountDto);
    TransactionState retrieveTransactionState(TransactionDto transactionDto);
    void cancelTransaction(TransactionDto transactionDto);
    long getRepositorySize();

}
