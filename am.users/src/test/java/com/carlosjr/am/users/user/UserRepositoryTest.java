package com.carlosjr.am.users.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void listOfLoggedUsers() {

        List<User> loggedUsers = userRepository
                .listOfLoggedUsers();
        assertThat(loggedUsers.size()).isEqualTo(0);

        ResponseEntity<UserTokenDto> signInResponse = testRestTemplate
                .withBasicAuth("jongreen", "green123")
                .getForEntity("/v1/users/signin", UserTokenDto.class);

        assertThat(signInResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<User> loggedUsers2 = userRepository
                .listOfLoggedUsers();
        assertThat(loggedUsers2.size()).isGreaterThan(0);


    }
}