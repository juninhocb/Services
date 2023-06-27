package com.carlosjr.am.users.roles;

import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserDto;
import com.carlosjr.am.users.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class RolesServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private RolesService rolesService;
    private User user;
    @BeforeEach
    void setUp() {
        user = userService.findUserById(userService.createNewUser(UserDto
                .builder()
                .fullName("Richard Test Red")
                .email("rts123@example.com")
                .password("richard123")
                .username("r198")
                .groupId(1L)
                .build()));
    }

    @AfterEach
    void tearDown() {
        userService.deleteUser(user.getId());
    }

    @Test
    void isAdmin() {
        boolean isAdmin = rolesService.isAdmin(user);
        assertThat(isAdmin).isFalse();
        userService.updateRoles(user.getId(), true);
        User userComparable = userService.findUserById(user.getId());
        isAdmin = rolesService.isAdmin(userComparable);
        assertThat(isAdmin).isTrue();
    }
}