package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.User;
import com.carlosjr.am.users.user.UserDto;
import com.carlosjr.am.users.user.UserMapper;
import com.carlosjr.am.users.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankAccountResourceTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    private BankAccountDto bankAccountDto;
    private static final String BASE_URL = "/v1/bank";
    @BeforeEach
    void setUp() {
        User user = userMapper.userDtoToUser(userService.findUserById(userService.createNewUser(UserDto
                .builder()
                .fullName("Carlos Eduardo Junior")
                .email("juninhocb@hotmail.com")
                .password("palmeiras2")
                .username("juninhocbb")
                .groupId(1L)
                .build())));

        bankAccountDto = BankAccountDto
                .builder()
                .userDto(userMapper.userToUserDto(user))
                .accountNumber(123143L)
                .name("Sicob 123")
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DirtiesContext
    public void shouldCreateBankAccountAndGetResourceLocation(){
        ResponseEntity<Void> createResponse = restTemplate
                .postForEntity(BASE_URL, bankAccountDto, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI uri = createResponse.getHeaders().getLocation();
        ResponseEntity<BankAccountDto> getResponse = restTemplate
                .getForEntity(uri, BankAccountDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void shouldGetNotNullFieldsOnFindByIdRequest(){
        ResponseEntity<Void> createResponse = restTemplate
                .postForEntity(BASE_URL, bankAccountDto, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI uri = createResponse.getHeaders().getLocation();
        ResponseEntity<BankAccountDto> getResponse = restTemplate
                .getForEntity(uri, BankAccountDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().amount()).isNotNull();
        assertThat(getResponse.getBody().name()).isNotNull();
        assertThat(getResponse.getBody().isActive()).isNotNull();
        assertThat(getResponse.getBody().accountNumber()).isNotNull();
        assertThat(getResponse.getBody().userDto()).isNotNull();
        assertThat(getResponse.getBody().id()).isNotNull();
    }

    @Test
    @DirtiesContext
    public void shouldNotCreateAnInvalidBankAccount(){
        BankAccountDto invalidBankAccountDto = BankAccountDto
                .builder()
                .userDto(null)
                .accountNumber(123143L)
                .name("Sicob 123")
                .build();
        ResponseEntity<Void> createResponse = restTemplate
                .postForEntity(BASE_URL, invalidBankAccountDto, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @DirtiesContext
    public void shouldUpdateBankAccountName(){
        ResponseEntity<Void> createResponse = restTemplate
                .postForEntity(BASE_URL, bankAccountDto, Void.class);
        URI uri = createResponse.getHeaders().getLocation();
        String fullUrl = String.format("%s?name=%s", uri, "otherName");
        ResponseEntity<Void> updateResponse = restTemplate
                .exchange(fullUrl, HttpMethod.PUT, null, Void.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DirtiesContext
    public void shouldNotUpdateBankAccountWithSameName(){
        ResponseEntity<Void> createResponse = restTemplate
                .postForEntity(BASE_URL, bankAccountDto, Void.class);
        URI uri = createResponse.getHeaders().getLocation();
        String fullUrl = String.format("%s?name=%s", uri, "Sicob 123");
        ResponseEntity<Void> updateResponse = restTemplate
                .exchange(fullUrl, HttpMethod.PUT, null, Void.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //fixme: get By Dto not entity
    @Test
    @DirtiesContext
    public void shouldToggleActiveBankAccount(){
        BankAccountDto mockedAccount =  bankAccountService.findAccountByAccountNumber(38423432L);
        ResponseEntity<Void> getToggleResponse = restTemplate
                .exchange(BASE_URL+"/toggle/"+mockedAccount.id(),
                        HttpMethod.PUT, null, Void.class);
        assertThat(getToggleResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<BankAccountDto> getResponse = restTemplate
                .getForEntity(BASE_URL+"/"+mockedAccount.id(), BankAccountDto.class);
        assertThat(getResponse.getBody().isActive()).isFalse();
    }
    @Test
    @DirtiesContext
    public void shouldDepositAndWithdrawValues(){
        BankAccountDto mockedAccount = bankAccountService.findAccountByAccountNumber(38423432L);
        ResponseEntity<Void> getDepositResponse = restTemplate
                .exchange(BASE_URL+"/deposit/"+mockedAccount.id()+"?amount=4",
                        HttpMethod.PUT, null, Void.class);
        assertThat(getDepositResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<BankAccountDto> getState1 = restTemplate
                .getForEntity(BASE_URL+"/"+mockedAccount.id(), BankAccountDto.class);
        assertThat(getState1.getBody().amount()).isNotZero();
        ResponseEntity<Void> getWithdrawResponse = restTemplate
                .exchange(BASE_URL+"/withdraw/"+mockedAccount.id()+"?amount=4",
                        HttpMethod.PUT, null, Void.class);
        assertThat(getWithdrawResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<BankAccountDto> getState2 = restTemplate
                .getForEntity(BASE_URL+"/"+mockedAccount.id(), BankAccountDto.class);
        assertThat(getState2.getBody().amount()).isZero();

    }

    @Test
    void shouldRetrieveBankAccountsByUser() {

        String url = BASE_URL + "/findbyemail/jongreen@example.com?page=0&size=3";

        ResponseEntity<Set<BankAccountDto>> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});

        System.out.println(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);



    }
}