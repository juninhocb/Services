package com.carlosjr.am.users.bank;

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
class bankAccountServiceTest {
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BankAccountMapper bankAccountMapper;
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
    void shouldUpdateAndRetrieveABankAccountById(){
        UUID uuid = getBankAccountUuid();
        String name = "Another label to this bank";
        bankAccountService.updateBankAccount(uuid, name);
        BankAccountDto updatedBankAccountDto = bankAccountService.findBankAccountById(uuid);
        assertThat(updatedBankAccountDto.name()).isEqualTo("Another label to this bank");
    }

    @Test
    @DirtiesContext
    void shouldToggleBankAccountActive(){
        UUID uuid = getBankAccountUuid();
        bankAccountService.toggleBankAccount(uuid);
        BankAccountDto updatedBankAccount = bankAccountService.findBankAccountById(uuid);
        assertThat(updatedBankAccount.isActive()).isEqualTo(false);
    }

    @Test
    @DirtiesContext
    void shouldDepositAmount(){
        UUID savedBankAccountDtoId = getBankAccountUuid();
        bankAccountService.depositAmount(38423432L, new BigDecimal(4));
        BankAccountDto updatedBankAccount = bankAccountService.findBankAccountById(savedBankAccountDtoId);
        assertThat(updatedBankAccount.amount()).isGreaterThan(new BigDecimal(0));
    }

    @Test
    @DirtiesContext
    void shouldWithdrawAmount(){
        UUID savedBankAccountDtoId = getBankAccountUuid();
        bankAccountService.withdrawAmount(38423432L, new BigDecimal(4));
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
            UserDto user = userService.findUserByUsername("juninhocbb");
            BankAccountDto bankAccountDto = BankAccountDto
                    .builder()
                    .userDto(user)
                    .accountNumber(123143L)
                    .name("Sicob 123")
                    .build();
            return bankAccountService.createNewBankAccount(bankAccountDto);
        }
        return uuidGeneral;
    }
}