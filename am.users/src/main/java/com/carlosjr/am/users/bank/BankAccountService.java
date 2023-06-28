package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.exceptions.SameFieldExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    public UUID createNewBankAccount(BankAccountDto bankAccountDto){
        BankAccount bankAccount = bankAccountMapper.bankAccountFromDto(bankAccountDto);
        bankAccount.setAmount(new BigDecimal(0));
        bankAccount.setIsActive(true);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return savedBankAccount.getId();
    }
    public long getRepositorySize(){
        return bankAccountRepository.count();
    }
    public BankAccountDto findBankAccountById(UUID id){
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
        return bankAccountMapper.bankAccountDtoFromEntity(bankAccount.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id))));
    }
    public void updateBankAccount(UUID id, BankAccountDto bankAccountDto){
        BankAccount oldBankAccount = retrieveBankAccountEntity(id);
        if (oldBankAccount.getName().equals(bankAccountDto.name())){
            throw new SameFieldExceptionHandler("The name is the same as the older name.");
        }
        oldBankAccount.setName(bankAccountDto.name());
        bankAccountRepository.save(oldBankAccount);
    }

    public void toggleBankAccount(UUID id){
        BankAccount oldBankAccount = retrieveBankAccountEntity(id);
        if (oldBankAccount.getIsActive()){
            oldBankAccount.setIsActive(false);
        }else {
            oldBankAccount.setIsActive(true);
        }
        bankAccountRepository.save(oldBankAccount);
    }

    public void depositAmount(UUID id, BigDecimal amount){
        BankAccount bankAccount = retrieveBankAccountEntity(id);
        BigDecimal currentAmount = bankAccount.getAmount();
        bankAccount.setAmount(currentAmount.add(amount));
        bankAccountRepository.save(bankAccount);
    }
    public void withdrawAmount(UUID id, BigDecimal amount){
        BankAccount bankAccount = retrieveBankAccountEntity(id);
        BigDecimal currentAmount = bankAccount.getAmount();
        bankAccount.setAmount(currentAmount.subtract(amount));
        bankAccountRepository.save(bankAccount);
    }

    //fixme
    public BankAccount retrieveBankAccountEntity(UUID id){
        return bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id)));
    }



}
