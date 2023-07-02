package com.carlosjr.am.users.boostrap;

import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.bank.BankAccountService;
import com.carlosjr.am.users.roles.Roles;
import com.carlosjr.am.users.roles.RolesService;
import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserMapper;
import com.carlosjr.am.users.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("test")
@Component
@RequiredArgsConstructor
public class MockData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BankAccountService bankAccountService;
    private final RolesService rolesService;
    private final UserMapper userMapper;
    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadUsers();
        loadBankAccounts();
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
        User u1 = User.builder()
                .groupId(1L)
                .fullName("Jon Green")
                .username("jongreen")
                .password("green123")
                .email("jongreen@example.com")
                .active(true)
                .roles(rolesService.findBasicRoles())
                .build();

        User u2 = User.builder()
                .groupId(1L)
                .fullName("Daniel Black")
                .username("danielblack")
                .password("black456")
                .email("danielblack@example.com")
                .active(true)
                .roles(rolesService.findBasicRoles())
                .build();

        User u3 = User.builder()
                .groupId(1L)
                .fullName("Jorge Blue")
                .username("jorgeblue")
                .password("blue789")
                .email("jorgeblue@example.com")
                .active(true)
                .roles(rolesService.findBasicRoles())
                .build();

        userRepository.saveAll(Arrays.asList(u1,u2,u3));
    }

    private void loadBankAccounts() {
        if(bankAccountService.getRepositorySize() == 0){
            BankAccountDto bankAccount1 = BankAccountDto.builder()
                    .accountNumber(38423432L)
                    .name("Bradesco 123")
                    .userDto(userMapper.userToUserDto(userRepository.getUserByUsername("jongreen")))
                    .build();
            BankAccountDto bankAccount2 = BankAccountDto.builder()
                    .accountNumber(38423433L)
                    .name("BB 123")
                    .userDto(userMapper.userToUserDto(userRepository.getUserByUsername("jongreen")))
                    .build();
            BankAccountDto bankAccount3 = BankAccountDto.builder()
                    .accountNumber(38423434L)
                    .name("Sicredi 123")
                    .userDto(userMapper.userToUserDto(userRepository.getUserByUsername("jongreen")))
                    .build();

            bankAccountService.createNewBankAccount(bankAccount1);
            bankAccountService.createNewBankAccount(bankAccount2);
            bankAccountService.createNewBankAccount(bankAccount3);
        }
    }
}
