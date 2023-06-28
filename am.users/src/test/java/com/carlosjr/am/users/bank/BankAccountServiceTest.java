package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserDto;
import com.carlosjr.am.users.user.UserMapper;
import com.carlosjr.am.users.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    private BankAccountDto bankAccountDto;
    private static User user;

    @BeforeEach
    void setUp() {
        userService.createNewUser(UserDto
                .builder()
                .fullName("Carlos Eduardo Junior")
                .email("juninhocb@hotmail.com")
                .password("palmeiras2")
                .username("juninhocbb")
                .groupId(1L)
                .build()
        );

        if (user == null){
            user = userService.findUserByUsername("juninhocbb");
        }

        if (bankAccountDto == null){
            bankAccountDto = BankAccountDto
                    .builder()
                    .user(user)
                    .accountNumber(123143L)
                    .name("Sicob 123")
                    .build();
        }
    }

    @Test
    @DirtiesContext
    void shouldCreateNewBankAccount() {
        long banksInDatabase = bankAccountService.getRepositorySize();
        System.out.println("database: " + banksInDatabase);  //ensure that has 3...
        UUID createdId = bankAccountService.createNewBankAccount(bankAccountDto);
        assertThat(createdId).isNotNull();
        assertThat(bankAccountService.getRepositorySize()).isEqualTo(banksInDatabase+1);
    }

    @Test
    @DirtiesContext
    void shouldCreateUpdateAndRetrieveABankAccountById(){
        UUID savedBankAccountDtoId = bankAccountService.createNewBankAccount(bankAccountDto);
        assertThat(savedBankAccountDtoId).isNotNull();
        BankAccountDto updateBankAccountDto = BankAccountDto
                .builder()
                .user(user)
                .accountNumber(123143L)
                .name("Another label to this bank")
                .build();
        bankAccountService.updateBankAccount(savedBankAccountDtoId, updateBankAccountDto);
        BankAccountDto updatedBankAccountDto = bankAccountService.findBankAccountById(savedBankAccountDtoId);
        assertThat(updatedBankAccountDto.name()).isEqualTo("Another label to this bank");
    }

    @Test
    @DirtiesContext
    void shouldToggleBankAccountActive(){
        UUID savedBankAccountDtoId = bankAccountService.createNewBankAccount(bankAccountDto);
        assertThat(savedBankAccountDtoId).isNotNull();
        bankAccountService.toggleBankAccount(savedBankAccountDtoId);
        //fixme: use DTO
        BankAccount updatedBankAccount = bankAccountService.retrieveBankAccountEntity(savedBankAccountDtoId);
        assertThat(updatedBankAccount.getIsActive()).isEqualTo(false);
    }

    @Test
    @DirtiesContext
    void shouldDepositAmount(){
        UUID savedBankAccountDtoId = bankAccountService.createNewBankAccount(bankAccountDto);
        assertThat(savedBankAccountDtoId).isNotNull();
        bankAccountService.depositAmount(savedBankAccountDtoId, new BigDecimal(4));
        BankAccountDto updatedBankAccount = bankAccountService.findBankAccountById(savedBankAccountDtoId);
        assertThat(updatedBankAccount.amount()).isGreaterThan(new BigDecimal(0));
    }

    @Test
    @DirtiesContext
    void shouldWithdrawAmount(){
        UUID savedBankAccountDtoId = bankAccountService.createNewBankAccount(bankAccountDto);
        assertThat(savedBankAccountDtoId).isNotNull();
        bankAccountService.withdrawAmount(savedBankAccountDtoId, new BigDecimal(4));
        BankAccountDto updatedBankAccount = bankAccountService.findBankAccountById(savedBankAccountDtoId);
        assertThat(updatedBankAccount.amount()).isNotZero();
    }




}