package com.carlosjr.am.invoices.invoice;

import com.carlosjr.am.common.InvoiceDto;
import org.springframework.data.domain.PageRequest;

import java.util.Set;
import java.util.UUID;

public interface InvoiceService {

    UUID saveNewInvoice(InvoiceDto invoiceDto);
    InvoiceDto findInvoiceById(UUID id);
    Invoice getPersistedInvoiceById(UUID id);
    Set<InvoiceDto> getInvoicesByUsername(String username, PageRequest pageRequest);
    long getRepositorySize();

}
