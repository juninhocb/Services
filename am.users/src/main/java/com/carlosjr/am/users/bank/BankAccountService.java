package com.carlosjr.am.users.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    public UUID createNewBankAccount( BankAccountDto bankAccountDto){
        BankAccount bankAccount = bankAccountMapper.bankAccountFromDto(bankAccountDto);
        bankAccount.setAmount(new BigDecimal(0));
        bankAccount.setIsActive(true);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return savedBankAccount.getId();
    }

    public long getRepositorySize(){
        return bankAccountRepository.count();
    }

}
