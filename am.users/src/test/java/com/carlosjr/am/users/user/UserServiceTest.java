package com.carlosjr.am.users.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    private UserDto testUserDto;
    @BeforeEach
    void setUp() {
        testUserDto = UserDto
                .builder()
                .fullName("Carlos Eduardo Junior")
                .email("juninhocb@hotmail.com")
                .password("palmeiras2")
                .username("juninhocb")
                .groupId(1L)
                .build();
    }
    @Test
    @DirtiesContext
    void createNewUserTest() {
        UUID id  = userService.createNewUser(testUserDto);
        assertThat(id).isNotNull();
    }
}