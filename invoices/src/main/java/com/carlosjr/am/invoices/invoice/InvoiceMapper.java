package com.carlosjr.am.invoices.invoice;

import com.carlosjr.am.common.InvoiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvoiceMapper {

    public Invoice invoiceDtoToEntity(InvoiceDto invoiceDto){
        log.trace("[ InvoiceMapper ] Converting invoice Dto to invoice entity");
        return Invoice.builder()
                .accountNumber(invoiceDto.accountNumber())
                .amount(invoiceDto.amount())
                .id(invoiceDto.invoiceId())
                .username(invoiceDto.username())
                .invoiceType(InvoiceType.valueOf(invoiceDto.invoiceType()))
                .build();
    };

    public InvoiceDto invoiceEntityToDto(Invoice invoice){
        log.trace("[ InvoiceMapper ] Converting invoice entity to invoice Dto");
        return InvoiceDto.builder()
                .invoiceType(invoice.getInvoiceType().toString())
                .accountNumber(invoice.getAccountNumber())
                .username(invoice.getUsername())
                .invoiceId(invoice.getId())
                .amount(invoice.getAmount())
                .build();
    };

}
