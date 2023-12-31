package com.carlosjr.am.invoices.invoice;

import com.carlosjr.am.common.InvoiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public UUID saveNewInvoice(InvoiceDto invoiceDto) {
        log.trace("[ invoiceServiceImpl ] Attempt to create an invoice: "
                + invoiceDto.accountNumber());
        Invoice savedInvoice = invoiceRepository
                .save(invoiceMapper.invoiceDtoToEntity(invoiceDto));
        log.trace("[ invoiceServiceImpl ] Invoice created successfully: "
                + savedInvoice.getId());
        return savedInvoice.getId();
    }

    @Override
    public InvoiceDto findInvoiceById(UUID id) {
        log.trace("[ invoiceServiceImpl ] Attempt to find an invoice: " + id);
        return invoiceMapper.invoiceEntityToDto(findPersistedByIdIntern(id));
    }

    @Override
    public Invoice getPersistedInvoiceById(UUID id) {
        return findPersistedByIdIntern(id);
    }

    @Override
    public Set<InvoiceDto> getInvoicesByUsername(String username, PageRequest pageRequest) {
        log.trace("[ invoiceServiceImpl ] Attempt to get list of invoices by username: "
                + username);

        List<Invoice> invoices =  invoiceRepository
                .getInvoicesByUsername(username, pageRequest);

         return invoices
                .stream()
                .map(invoiceMapper::invoiceEntityToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public long getRepositorySize() {
        return invoiceRepository.count();
    }

    private Invoice findPersistedByIdIntern(UUID id){
        log.trace("[ invoiceServiceImpl ] Attempt to find intern an invoice: " + id);
        Optional<Invoice> invoiceOpt = invoiceRepository
                .findById(id);
        if (invoiceOpt.isPresent()){
            log.trace("[ invoiceServiceImpl ] Invoice found ");
            return invoiceOpt.get();
        } else {
            log.trace("[ invoiceServiceImpl ] Invoice not found ");
            throw new RuntimeException();
        }
    }

}
