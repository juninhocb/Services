package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccountDto;
import org.springframework.data.domain.PageRequest;

import java.util.Set;
import java.util.UUID;

public interface TransactionService {
    UUID createNewTransaction(TransactionDto transactionDto);
    TransactionDto findTransactionById(UUID id);
    Set<TransactionDto> retrieveTransactionsByBankAccount(BankAccountDto bankAccountDto,
                                                          PageRequest pageRequest);
    TransactionDto findTransactionByInvoiceId(UUID invoiceId);
    long getRepositorySize();

}
