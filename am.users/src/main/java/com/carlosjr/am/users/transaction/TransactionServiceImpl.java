package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.user.UserDto;
import kotlin.NotImplementedError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    @Override
    public UUID createNewTransaction(TransactionDto transactionDto) {
        Transaction transaction = transactionRepository
                .save(transactionMapper.fromDtoToEntity(transactionDto));
        return transaction.getId();
    }
    @Override
    public TransactionDto findTransactionById(UUID id) {
        return null;
    }

    @Override
    public Set<TransactionDto> retrieveTransactionsByUser(UserDto userDto) {
        return null;
    }

    @Override
    public Set<TransactionDto> retrieveTransactionsByGroupId(Long groupId) {
        return null;
    }

    @Override
    public Set<TransactionDto> retrieveTransactionsByBankAccount(BankAccountDto bankAccountDto) {
        return null;
    }

    @Override
    public TransactionState retrieveTransactionState(TransactionDto transactionDto) {
        return null;
    }

    @Override
    public void cancelTransaction(TransactionDto transactionDto) {
        throw new NotImplementedError();
    }

    @Override
    public long getRepositorySize() {
        return transactionRepository.count();
    }
}
