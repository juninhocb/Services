package com.carlosjr.am.users.boostrap;

import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.bank.BankAccountService;
import com.carlosjr.am.users.roles.Roles;
import com.carlosjr.am.users.roles.RolesService;
import com.carlosjr.am.users.transaction.TransactionDto;
import com.carlosjr.am.users.transaction.TransactionService;
import com.carlosjr.am.users.transaction.TransactionState;
import com.carlosjr.am.users.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Profile("test")
@Component
@RequiredArgsConstructor
public class MockData implements CommandLineRunner {
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final RolesService rolesService;
    private final TransactionService transactionService;
    private final UserMapper userMapper;
    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadUsers();
        loadBankAccounts();
        loadTransactions();
    }
    private void loadRoles(){
        if(rolesService.getRepositoryCount() == 0){
            Roles role1 = Roles.builder().id(1L).isBasic(true).name("write-resources").build();
            Roles role2 = Roles.builder().id(2L).isBasic(true).name("read-resources").build();
            Roles role3 = Roles.builder().id(3L).isBasic(false).name("manage-users").build();
            Roles role4 = Roles.builder().id(4L).isBasic(false).name("manage-system").build();
            rolesService.mockRoles(Arrays.asList(role1, role2, role3, role4));

        }
    }
    private void loadUsers(){
        UserDto u1 = UserDto.builder()
                .groupId(1L)
                .fullName("Jon Green")
                .username("jongreen")
                .password("green123")
                .email("jongreen@example.com")
                .active(true)
                .roles(rolesService.findBasicRoles())
                .build();

        UserDto u2 = UserDto.builder()
                .groupId(1L)
                .fullName("Daniel Black")
                .username("danielblack")
                .password("black456")
                .email("danielblack@example.com")
                .active(true)
                .roles(rolesService.findBasicRoles())
                .build();

        UserDto u3 = UserDto.builder()
                .groupId(1L)
                .fullName("Jorge Blue")
                .username("jorgeblue")
                .password("blue789")
                .email("jorgeblue@example.com")
                .active(true)
                .roles(rolesService.findBasicRoles())
                .build();

        userService.createNewUser(u1);
        userService.createNewUser(u2);
        userService.createNewUser(u3);

    }
    private void loadBankAccounts() {
        if(bankAccountService.getRepositorySize() == 0){
            BankAccountDto bankAccount1 = BankAccountDto.builder()
                    .accountNumber(38423432L)
                    .name("Bradesco 123")
                    .userDto(userService.findUserByUsername("jongreen"))
                    .build();
            BankAccountDto bankAccount2 = BankAccountDto.builder()
                    .accountNumber(38423433L)
                    .name("BB 123")
                    .userDto(userService.findUserByUsername("jongreen"))
                    .build();
            BankAccountDto bankAccount3 = BankAccountDto.builder()
                    .accountNumber(38423434L)
                    .name("Sicredi 123")
                    .userDto(userService.findUserByUsername("jongreen"))
                    .build();

            bankAccountService.createNewBankAccount(bankAccount1);
            bankAccountService.createNewBankAccount(bankAccount2);
            bankAccountService.createNewBankAccount(bankAccount3);
        }
    }
    private void loadTransactions() {
        if (transactionService.getRepositorySize() == 0){

            BankAccountDto bankAccountDto = bankAccountService.findAccountByAccountNumber(38423434L);
            UserDto userDto = userService.findUserByUsername("jongreen");

            TransactionDto t1 = TransactionDto.builder()
                    .state(TransactionState.RELEASED)
                    .bankAccountDto(bankAccountDto)
                    .userDto(userDto)
                    .amount(new BigDecimal(3))
                    .build();

            TransactionDto t2 = TransactionDto.builder()
                    .state(TransactionState.RELEASED)
                    .bankAccountDto(bankAccountDto)
                    .userDto(userDto)
                    .amount(new BigDecimal(6))
                    .build();

            TransactionDto t3 = TransactionDto.builder()
                    .state(TransactionState.RELEASED)
                    .bankAccountDto(bankAccountDto)
                    .userDto(userDto)
                    .amount(new BigDecimal(4))
                    .build();

            transactionService.createNewTransaction(t1);
            transactionService.createNewTransaction(t2);
            transactionService.createNewTransaction(t3);

        }
    }
}
