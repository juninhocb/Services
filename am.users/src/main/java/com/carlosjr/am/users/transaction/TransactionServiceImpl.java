package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccount;
import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.bank.BankAccountService;
import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BankAccountService bankAccountService;
    @Override
    public UUID createNewTransaction(TransactionDto transactionDto) {
        Transaction transaction = transactionRepository
                .save(transactionMapper.fromDtoToEntity(transactionDto));
        return transaction.getId();
    }
    @Override
    public TransactionDto findTransactionById(UUID id) {
        Optional<Transaction> transactionOptional = transactionRepository
                .findById(id);
        if (transactionOptional.isEmpty()){
            throw new ResourceNotFoundException("Resource with id " + id + " was not found.");
        }
        return transactionMapper.fromEntityToDto(transactionOptional.get());
    }
    @Override
    public Set<TransactionDto> retrieveTransactionsByBankAccount(BankAccountDto bankAccountDto,
                                                                 PageRequest pageRequest) {

        BankAccount persistedBankAccount = bankAccountService
                .findPersistedByAccountNumber(bankAccountDto.accountNumber());

        List<Transaction> transactions = transactionRepository
                .findTransactionsByBankAccount(persistedBankAccount, pageRequest);

        Set<TransactionDto> transactionDtos = transactions
                .stream()
                .map(transactionMapper::fromEntityToDto)
                .collect(Collectors.toSet());

        return transactionDtos;
    }

    @Override
    public TransactionDto findTransactionByInvoiceId(UUID invoiceId) {
        Optional<Transaction> persistedTransactionOptional = transactionRepository
                .findTransactionByInvoiceId(invoiceId);

        if(persistedTransactionOptional.isEmpty()){
            throw new ResourceNotFoundException("Resource with id " + invoiceId + " was not found.");
        }
        return transactionMapper.fromEntityToDto(persistedTransactionOptional.get());
    }

    @Override
    public long getRepositorySize() {
        return transactionRepository.count();
    }
}
