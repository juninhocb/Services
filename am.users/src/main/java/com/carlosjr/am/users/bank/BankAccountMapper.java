package com.carlosjr.am.users.bank;

import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccount bankAccountFromDto(BankAccountDto bankAccountDto){
        return BankAccount.builder()
                .amount(bankAccountDto.amount())
                .accountNumber(bankAccountDto.accountNumber())
                .name(bankAccountDto.name())
                .user(bankAccountDto.user())
                .build();
    }

}
