package com.carlosjr.am.users.user;

import com.carlosjr.am.users.roles.RolesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserResourceTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RolesService rolesService;
    private UserDto testUserDto;
    private final String BASE_URL = "/v1/users";
    private URI resourcePath;
    private String idResource;
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
        ResponseEntity<Void> getUriOfCreatedUser = restTemplate
                .postForEntity(BASE_URL, testUserDto, Void.class);
        resourcePath = getUriOfCreatedUser.getHeaders().getLocation();
        idResource = resourcePath.toString().split(BASE_URL+"/")[1];

    }
    @AfterEach
    void tearDown(){
        restTemplate.exchange(BASE_URL+idResource, HttpMethod.DELETE, null, Void.class);
    }

    @Test
    @DirtiesContext
    void shouldCreateAndGetLocationOfAValidResource(){
        ResponseEntity<Void> getCreateResponse = restTemplate
                .postForEntity(
                        BASE_URL,
                        UserDto.builder()
                                .fullName("Alfredo John")
                                .email("af123@example.com")
                                .password("af123")
                                .username("alfredo.j")
                                .groupId(1L)
                                .build(),
                        Void.class);
        assertThat(getCreateResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI uri = getCreateResponse.getHeaders().getLocation();
        ResponseEntity<User> getResourceResponse = restTemplate
                .getForEntity(uri, User.class);
        assertThat(getResourceResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        User createdUser = getResourceResponse.getBody();
        assertThat(createdUser.getCreatedDate()).isNotNull();
        assertThat(createdUser.getActive()).isNotNull();
        assertThat(createdUser.getRoles()).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("af123@example.com");
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
                .getForEntity(BASE_URL+"/"+UUID.randomUUID(), User.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DirtiesContext
    void shouldUpdateUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Void> getUpdateResponse = restTemplate
                .exchange(resourcePath, HttpMethod.PUT, new HttpEntity<>(UserDto.builder()
                        .fullName("Alfredo John")
                        .email("af123@example.com")
                        .password("af123")
                        .username("alfredo.j")
                        .groupId(1L)
                        .build(), headers), Void.class );
        assertThat(getUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<User> getResourceAgain = restTemplate
                .getForEntity(resourcePath, User.class);
        assertThat(getResourceAgain.getBody().getEmail()).isEqualTo("af123@example.com");
    }
    @Test
    @DirtiesContext
    void shouldDeleteResource(){
        ResponseEntity<Void> getDeleteResponse = restTemplate
                .exchange(resourcePath, HttpMethod.DELETE, null, Void.class);
        assertThat(getDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<UserDto> getFindResponse = restTemplate
                .getForEntity(resourcePath, UserDto.class);
        assertThat(getFindResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DirtiesContext
    void shouldUpdateRoles(){
        ResponseEntity<Void> getUpdateRoleResponse = restTemplate
                .exchange(BASE_URL+"/roles/" + idResource + "?isAdmin=true", HttpMethod.PUT, null, Void.class);
        assertThat(getUpdateRoleResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<User> getUpdatedResource = restTemplate
                .getForEntity(resourcePath, User.class);
        User user = getUpdatedResource.getBody();
        //fixme: incorrect update, but user was really updated in database...
        assertThat(rolesService.isAdmin(user)).isTrue();

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