package com.carlosjr.am.invoices.invoice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void getInvoicesByUsername() {
        List<Invoice> invoices = invoiceRepository
                .getInvoicesByUsername("jongreen", PageRequest.of(0,2));
        assertThat(invoices.size()).isGreaterThan(0);
        System.out.println(invoices.size());

    }
}