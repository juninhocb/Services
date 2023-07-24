package com.carlosjr.am.invoices.invoice;

import com.carlosjr.am.common.InvoiceDto;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InvoiceResourceTest {

    @Autowired
    private InvoiceResource invoiceResource;
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    private InvoiceDto invoiceDto;
    @BeforeEach
    void setUp() {

        invoiceDto = InvoiceDto.builder()
                .invoiceType("PAYMENT")
                .accountNumber(38423432L)
                .amount(new BigDecimal(3))
                .username("jongreen")
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldCreateAnInvoiceAndRetrieveValidResourcePath() {

        ResponseEntity<Void> createResource = restTemplate
                .postForEntity("/v1/invoices", invoiceDto, Void.class);

        assertThat(createResource.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI resourcePath = createResource
                .getHeaders().getLocation();

        ResponseEntity<InvoiceDto> getCreatedResource = restTemplate
                .getForEntity(resourcePath, InvoiceDto.class);

        assertThat(getCreatedResource.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCreatedResource.getBody()).isNotNull();

        System.out.println("Created dto \n" + getCreatedResource.getBody());

    }

    @Test
    void shouldRetrieveSetOfInvoiceDtos(){
        String url = "/v1/invoices/findall/jongreen?page=0&size=3";

        ResponseEntity<Set<InvoiceDto>> getInvoices = restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Set<InvoiceDto>>() {
                        } );

        assertThat(getInvoices.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getInvoices.getBody().size()).isGreaterThan(0);
    }

}