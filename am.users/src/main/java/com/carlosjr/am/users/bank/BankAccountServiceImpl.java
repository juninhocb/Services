package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.exceptions.SameFieldExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
@Service
@Primary
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    @Override
    public UUID createNewBankAccount(BankAccountDto bankAccountDto){
        BankAccount bankAccount = bankAccountMapper.bankAccountFromDto(bankAccountDto);
        bankAccount.setAmount(new BigDecimal(0));
        bankAccount.setIsActive(true);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return savedBankAccount.getId();
    }
    @Override
    public long getRepositorySize(){
        return bankAccountRepository.count();
    }
    @Override
    public BankAccountDto findBankAccountById(UUID id){
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
        return bankAccountMapper.bankAccountDtoFromEntity(bankAccount.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id))));
    }
    @Override
    public void updateBankAccount(UUID id, String name){
        BankAccount oldBankAccount = retrieveBankAccountEntity(id);
        if (oldBankAccount.getName().equals(name)){
            throw new SameFieldExceptionHandler("The name is the same as the older name.");
        }
        oldBankAccount.setName(name);
        bankAccountRepository.save(oldBankAccount);
    }
    @Override
    public void toggleBankAccount(UUID id){
        BankAccount oldBankAccount = retrieveBankAccountEntity(id);
        if (oldBankAccount.getIsActive()){
            oldBankAccount.setIsActive(false);
        }else {
            oldBankAccount.setIsActive(true);
        }
        bankAccountRepository.save(oldBankAccount);
    }
    @Override
    public void depositAmount(UUID id, BigDecimal amount){
        BankAccount bankAccount = retrieveBankAccountEntity(id);
        BigDecimal currentAmount = bankAccount.getAmount();
        bankAccount.setAmount(currentAmount.add(amount));
        bankAccountRepository.save(bankAccount);
    }
    @Override
    public void withdrawAmount(UUID id, BigDecimal amount){
        BankAccount bankAccount = retrieveBankAccountEntity(id);
        BigDecimal currentAmount = bankAccount.getAmount();
        bankAccount.setAmount(currentAmount.subtract(amount));
        bankAccountRepository.save(bankAccount);
    }
    public BankAccountDto findAccountByAccountNumber(Long accountNumber){
        return bankAccountMapper
                .bankAccountDtoFromEntity(bankAccountRepository
                .findByAccountNumber(accountNumber));
    }

    @Override
    public BankAccount findPersistedByAccountNumber(Long accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

    protected BankAccount retrieveBankAccountEntity(UUID id){
        return bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id)));
    }



}
