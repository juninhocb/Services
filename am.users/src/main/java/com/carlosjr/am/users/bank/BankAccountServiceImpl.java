package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.exceptions.SameFieldExceptionHandler;
import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;

    @Override
    public UUID createNewBankAccount(BankAccountDto bankAccountDto){
        log.trace("[ BankAccountServiceImpl ] Attempt to create a new bank account");
        BankAccount bankAccount = bankAccountMapper.bankAccountFromDto(bankAccountDto);
        bankAccount.setAmount(new BigDecimal(0));
        bankAccount.setIsActive(true);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        log.trace("[ BankAccountServiceImpl ] Successfully bank account created: " + savedBankAccount.getId());
        return savedBankAccount.getId();
    }
    @Override
    public long getRepositorySize(){
        return bankAccountRepository.count();
    }
    @Override
    public BankAccountDto findBankAccountById(UUID id){
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
        log.trace("Attempt to find bank account Dto by id: " + id);
        return bankAccountMapper.bankAccountDtoFromEntity(bankAccount.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id))));
    }
    @Override
    public void updateBankAccount(UUID id, String name){
        BankAccount oldBankAccount = retrieveBankAccountEntity(id);
        if (oldBankAccount.getName().equals(name)){
            log.warn("T[ BankAccountServiceImpl ] This name is the same as the older name: "
                    + name);
            throw new SameFieldExceptionHandler("The name is the same as the older name.");
        }
        oldBankAccount.setName(name);
        bankAccountRepository.save(oldBankAccount);
        log.trace("[ BankAccountServiceImpl ] Bank account name changed successfully");
    }
    @Override
    public void toggleBankAccount(UUID id){
        BankAccount oldBankAccount = retrieveBankAccountEntity(id);
        if (oldBankAccount.getIsActive()){
            oldBankAccount.setIsActive(false);
        }else {
            oldBankAccount.setIsActive(true);
        }
        BankAccount savedBankAccount = bankAccountRepository.save(oldBankAccount);
        log.trace("[ BankAccountServiceImpl ] Bank account toggled successfully the new status is: "
                + savedBankAccount.getIsActive());
    }
    @Override
    public BankAccountDto depositAmount(Long bankAccountNumber, BigDecimal amount){
        BankAccount bankAccount = findPersistedByAccountNumber(bankAccountNumber);
        BigDecimal currentAmount = bankAccount.getAmount();
        bankAccount.setAmount(currentAmount.add(amount));
        BankAccount bankAccountSaved = bankAccountRepository.save(bankAccount);
        log.trace("[ BankAccountServiceImpl ] Successfully deposited amount "
            + amount + " actual value is: " + bankAccountSaved.getAmount());
        return bankAccountMapper.bankAccountDtoFromEntity(bankAccountSaved);
    }
    @Override
    public BankAccountDto withdrawAmount(Long bankAccountNumber, BigDecimal amount){
        BankAccount bankAccount = findPersistedByAccountNumber(bankAccountNumber);
        BigDecimal currentAmount = bankAccount.getAmount();
        bankAccount.setAmount(currentAmount.subtract(amount));
        BankAccount bankAccountSaved = bankAccountRepository.save(bankAccount);
        log.trace("[ BankAccountServiceImpl ] Successfully withdraw amount "
                + amount + " actual value is: " + bankAccountSaved.getAmount());
        return bankAccountMapper.bankAccountDtoFromEntity(bankAccountSaved);
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

    @Override
    public Set<BankAccountDto> retrieveBankAccountsByUser(String email, PageRequest pageRequest) {
        log.trace("[ BankAccountServiceImpl ] Attempt to find user of user with email: " + email);
        User persistedUser = userService
                .findPersistedUserByEmail(email);

        List<BankAccount> listOfBanks = bankAccountRepository
                .findAccountsByUser(persistedUser, pageRequest);

        Set<BankAccountDto> setOfBanksDto = listOfBanks
                .stream()
                .map(bankAccountMapper::bankAccountDtoFromEntity)
                .collect(Collectors.toSet());

        log.trace("[ BankAccountServiceImpl ] Find users number: " + listOfBanks.size());
        return setOfBanksDto;
    }

    protected BankAccount retrieveBankAccountEntity(UUID id){
        return bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id)));
    }



}
