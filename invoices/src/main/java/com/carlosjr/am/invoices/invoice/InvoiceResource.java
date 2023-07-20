package com.carlosjr.am.invoices.invoice;

import com.carlosjr.am.invoices.common.InvoiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceResource {

    private final InvoiceService invoiceService;
    @PostMapping
    public ResponseEntity<Void> saveInvoice(@RequestBody InvoiceDto invoiceDto,
                                            UriComponentsBuilder ucb){
        log.trace("[ invoiceResources ] Saving a new invoice: "
                + invoiceDto.username());
        UUID createdId = invoiceService
                .saveNewInvoice(invoiceDto);
        URI resourcePath = ucb
                .path("/v1/invoices/{invoiceId}")
                .buildAndExpand(createdId)
                .toUri();

        return ResponseEntity.created(resourcePath).build();
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDto> getInvoiceById(
            @PathVariable(name = "invoiceId") UUID id){

        InvoiceDto invoiceDto = invoiceService
                .findInvoiceById(id);
        return ResponseEntity.ok().body(invoiceDto);
    }

    @GetMapping("/findall/{username}")
    public ResponseEntity<Set<InvoiceDto>> getAllByUsername(
            @PathVariable(name = "username") String username,
            Pageable pageable){

        Set<InvoiceDto> invoiceDtos = invoiceService
                .getInvoicesByUsername(username, PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "createdInvoice"))));;

        return ResponseEntity.ok().body(invoiceDtos);

    }


}
