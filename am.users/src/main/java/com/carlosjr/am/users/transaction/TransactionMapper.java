package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccountMapper;
import com.carlosjr.am.users.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;

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
        return Transaction.builder()
                .bankAccount(bankAccountMapper.bankAccountFromDto(transactionDto.bankAccountDto()))
                .amount(transactionDto.amount())
                .invoiceId(transactionDto.invoiceId())
                .build();
    }

}
