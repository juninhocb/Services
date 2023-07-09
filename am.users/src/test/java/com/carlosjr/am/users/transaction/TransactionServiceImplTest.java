package com.carlosjr.am.users.transaction;

import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.bank.BankAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
    @Qualifier("transactionServiceImpl")
    private TransactionService transactionService;
    private TransactionDto transactionDto;
    private BankAccountDto bankAccountDto;
    @Autowired
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        bankAccountDto = bankAccountService.findAccountByAccountNumber(38423432L);
        transactionDto = TransactionDto.builder()
                .bankAccountDto(bankAccountDto)
                .invoiceId(UUID.randomUUID())
                .amount(new BigDecimal(12))
                .build();
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    @DirtiesContext
    public void createNewTransactionAndFindByIdAndInvoiceId(){
        long repositoryCount = transactionService.getRepositorySize();
        UUID uuidResponse = transactionService.createNewTransaction(transactionDto);
        assertThat(uuidResponse).isNotNull();
        assertThat(transactionService.getRepositorySize()).isEqualTo(repositoryCount+1);
        TransactionDto transactionDtoFromDataBase = transactionService
                .findTransactionById(uuidResponse);
        assertThat(transactionDtoFromDataBase).isNotNull();
        TransactionDto transactionDtoFromDataBase2 = transactionService
                .findTransactionByInvoiceId(transactionDtoFromDataBase.invoiceId());
        assertThat(transactionDtoFromDataBase2).isNotNull();
    }

    @Test
    @DirtiesContext
    public void createNewTransactionAndRetrieveTransactionsByBankAccount(){
        UUID uuidResponse = transactionService.createNewTransaction(transactionDto);
        assertThat(uuidResponse).isNotNull();
        Set<TransactionDto> transactionDtos = transactionService
                .retrieveTransactionsByBankAccount(bankAccountDto,
                                                    PageRequest.of(0,5));
        assertThat(transactionDtos).hasSizeGreaterThan(0);


    }




}