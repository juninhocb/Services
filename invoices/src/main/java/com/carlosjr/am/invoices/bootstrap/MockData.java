package com.carlosjr.am.invoices.bootstrap;

import com.carlosjr.am.common.InvoiceDto;
import com.carlosjr.am.invoices.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class MockData implements CommandLineRunner {

    private final InvoiceService invoiceService;
    @Override
    public void run(String... args) throws Exception {

        if (invoiceService.getRepositorySize() == 0){

            InvoiceDto i1 = InvoiceDto.builder()
                    .invoiceType("EARN")
                    .accountNumber(38423432L)
                    .username("jongreen")
                    .amount(new BigDecimal(12))
                    .build();

            InvoiceDto i2 = InvoiceDto.builder()
                    .invoiceType("PAYMENT")
                    .accountNumber(38423432L)
                    .username("jongreen")
                    .amount(new BigDecimal(6))
                    .build();

            InvoiceDto i3 = InvoiceDto.builder()
                    .invoiceType("PAYMENT")
                    .accountNumber(38423432L)
                    .username("jongreen")
                    .amount(new BigDecimal(2))
                    .build();

            invoiceService.saveNewInvoice(i1);
            invoiceService.saveNewInvoice(i2);
            invoiceService.saveNewInvoice(i3);

        }

        log.trace("[ mockData ] Successfully loades 3 items");

    }
}
