package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccount;
import com.carlosjr.am.users.bank.BankAccountMapper;
import com.carlosjr.am.users.bank.BankAccountService;
import com.carlosjr.am.users.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final BankAccountService bankAccountService;

    public TransactionDto fromEntityToDto (Transaction transaction){
        return TransactionDto.builder()
                .id(transaction.getId())
                .bankAccountDto(bankAccountMapper.bankAccountDtoFromEntity(transaction.getBankAccount()))
                .invoiceId(transaction.getInvoiceId())
                .createdTime(transaction.getCreatedTime().toLocalDateTime())
                .amount(transaction.getAmount())
                .build();
    }

    public Transaction fromDtoToEntity(TransactionDto transactionDto){
        BankAccount persistedBankAccount = bankAccountService
                .findPersistedByAccountNumber(transactionDto.bankAccountDto().accountNumber());
        return Transaction.builder()
                .bankAccount(persistedBankAccount)
                .amount(transactionDto.amount())
                .invoiceId(transactionDto.invoiceId())
                .build();
    }

}
