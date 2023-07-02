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
                .state(transaction.getState())
                .createdTime(transaction.getCreatedTime().toLocalDateTime())
                .amount(transaction.getAmount())
                .userDto(userMapper.userToUserDto(transaction.getUser()))
                .build();
    }

    public Transaction fromDtoToEntity(TransactionDto transactionDto){
        return Transaction.builder()
                .bankAccount(bankAccountMapper.bankAccountFromDto(transactionDto.bankAccountDto()))
                .user(userMapper.userDtoToUser(transactionDto.userDto()))
                .amount(transactionDto.amount())
                .state(transactionDto.state())
                .build();
    }

}
