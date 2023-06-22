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
    private final String BASE_URL = "/v1/users";
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
                .postForEntity(BASE_URL, testUserDto, Void.class);
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
    @Test
    void shouldNotSaveAnInvalidUserDto(){
        ResponseEntity<Void> getCreateResponse = restTemplate
                .postForEntity(BASE_URL, getInvalidDto(InvalidUserDto.MISSING_DATA), Void.class);
        assertThat(getCreateResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseEntity<Void> getCreateResponse2 = restTemplate
                .postForEntity(BASE_URL, getInvalidDto(InvalidUserDto.INVALID_USERNAME), Void.class);
        assertThat(getCreateResponse2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseEntity<Void> getCreateResponse3 = restTemplate
                .postForEntity(BASE_URL, getInvalidDto(InvalidUserDto.INVALID_EMAIL), Void.class);
        assertThat(getCreateResponse3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseEntity<Void> getCreateResponse4 = restTemplate
                .postForEntity(BASE_URL, getInvalidDto(InvalidUserDto.INVALID_GROUP_ID), Void.class);
        assertThat(getCreateResponse4.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ResponseEntity<Void> getCreateResponse5 = restTemplate
                .postForEntity(BASE_URL, getInvalidDto(InvalidUserDto.INVALID_NAME), Void.class);
        assertThat(getCreateResponse5.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void shouldRespondWithNotFoundWhenResourceIsNotFound(){
        ResponseEntity<User> getResponse = restTemplate
                .getForEntity(BASE_URL+"/99", User.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void shouldUpdateUser(){

    }

    private UserDto getInvalidDto(InvalidUserDto invalidUserDto) {
        UserDto.UserDtoBuilder builder = UserDto.builder()
                .fullName("Testing")
                .groupId(1L)
                .username("user")
                .password("pass")
                .email("juninhocb@hotmail.com");

        switch (invalidUserDto) {
            case INVALID_NAME:
                builder.fullName("123"); //name with numbers
                break;
            case INVALID_EMAIL:
                builder.email("ufsdhuf");
                break;
            case INVALID_GROUP_ID:
                builder.groupId(-1L); //negative value
                break;
            case INVALID_USERNAME:
                builder.username("ab"); //only two letter username
                break;
            case MISSING_DATA:
                builder.username(null);
                break;
        }

        return builder.build();
    }
    private enum InvalidUserDto {
        INVALID_NAME,
        INVALID_EMAIL,
        INVALID_GROUP_ID,
        INVALID_USERNAME,
        MISSING_DATA
    }


}