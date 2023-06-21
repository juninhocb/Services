package com.carlosjr.am.users.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserResourceTest {
    @Autowired
    private TestRestTemplate restTemplate;
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
    void shouldCreateAndGetLocationOfAValidResource(){
        ResponseEntity<Void> getCreateResponse = restTemplate
                .postForEntity("/v1/users", testUserDto, Void.class);
        assertThat(getCreateResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI uri = getCreateResponse.getHeaders().getLocation();
        ResponseEntity<User> getResourceResponse = restTemplate
                .getForEntity(uri, User.class);
        assertThat(getResourceResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        User createdUser = getResourceResponse.getBody();
        assertThat(createdUser.getCreatedDate()).isNotNull();
        assertThat(createdUser.getActive()).isNotNull();
        assertThat(createdUser.getRoles()).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("juninhocb@hotmail.com");
    }

}