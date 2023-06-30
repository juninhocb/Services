package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserDto;
import com.carlosjr.am.users.user.UserMapper;
import com.carlosjr.am.users.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BankAccountServiceTest {
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    private static UUID uuidGeneral = null;

    @Test
    @DirtiesContext
    void shouldCreateNewBankAccount() {
        long banksInDatabase = bankAccountService.getRepositorySize();
        System.out.println("database: " + banksInDatabase);  //ensure that has 3...
        getBankAccountUuid();
        assertThat(bankAccountService.getRepositorySize()).isEqualTo(banksInDatabase+1);
    }

    @Test
    @DirtiesContext
    void shouldCreateUpdateAndRetrieveABankAccountById(){
        UUID uuid = getBankAccountUuid();
        BankAccountDto updateBankAccountDto = BankAccountDto
                .builder()
                .user(bankAccountService.findBankAccountById(uuid).user())
                .accountNumber(123143L)
                .name("Another label to this bank")
                .build();
        bankAccountService.updateBankAccount(uuid, updateBankAccountDto);
        BankAccountDto updatedBankAccountDto = bankAccountService.findBankAccountById(uuid);
        assertThat(updatedBankAccountDto.name()).isEqualTo("Another label to this bank");
    }

    @Test
    @DirtiesContext
    void shouldToggleBankAccountActive(){
        UUID uuid = getBankAccountUuid();
        bankAccountService.toggleBankAccount(uuid);
        BankAccount updatedBankAccount = bankAccountService.retrieveBankAccountEntity(uuid);
        assertThat(updatedBankAccount.getIsActive()).isEqualTo(false);
    }

    @Test
    @DirtiesContext
    void shouldDepositAmount(){
        UUID savedBankAccountDtoId = getBankAccountUuid();
        bankAccountService.depositAmount(savedBankAccountDtoId, new BigDecimal(4));
        BankAccountDto updatedBankAccount = bankAccountService.findBankAccountById(savedBankAccountDtoId);
        assertThat(updatedBankAccount.amount()).isGreaterThan(new BigDecimal(0));
    }

    @Test
    @DirtiesContext
    void shouldWithdrawAmount(){
        UUID savedBankAccountDtoId = getBankAccountUuid();
        bankAccountService.withdrawAmount(savedBankAccountDtoId, new BigDecimal(4));
        BankAccountDto updatedBankAccount = bankAccountService.findBankAccountById(savedBankAccountDtoId);
        assertThat(updatedBankAccount.amount()).isNotZero();
    }

    private UUID getBankAccountUuid(){
        if (uuidGeneral == null){
            userService.createNewUser(UserDto
                    .builder()
                    .fullName("Carlos Eduardo Junior")
                    .email("juninhocb@hotmail.com")
                    .password("palmeiras2")
                    .username("juninhocbb")
                    .groupId(1L)
                    .build()
            );
            User user = userService.findUserByUsername("juninhocbb");
            BankAccountDto bankAccountDto = BankAccountDto
                    .builder()
                    .user(user)
                    .accountNumber(123143L)
                    .name("Sicob 123")
                    .build();
            return bankAccountService.createNewBankAccount(bankAccountDto);
        }
        return uuidGeneral;
    }
}