package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {

    private final UserMapper userMapper;

    public BankAccount bankAccountFromDto(BankAccountDto bankAccountDto){
        return BankAccount.builder()
                .amount(bankAccountDto.amount())
                .accountNumber(bankAccountDto.accountNumber())
                .name(bankAccountDto.name())
                .user(userMapper.userDtoToUser(bankAccountDto.userDto()))
                .build();
    }
    public BankAccountDto bankAccountDtoFromEntity(BankAccount bankAccount){
        return BankAccountDto.builder()
                .id(bankAccount.getId())
                .name(bankAccount.getName())
                .accountNumber(bankAccount.getAccountNumber())
                .userDto(userMapper.userToUserDto(bankAccount.getUser()))
                .amount(bankAccount.getAmount())
                .isActive(bankAccount.getIsActive())
                .build();
    }

}
