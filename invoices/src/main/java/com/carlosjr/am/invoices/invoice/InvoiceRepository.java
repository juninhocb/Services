package com.carlosjr.am.invoices.invoice;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    @Query("SELECT i FROM Invoice i WHERE i.username = :username")
    List<Invoice> getInvoicesByUsername (@Param("username") String username,
                                         PageRequest pageRequest);
}
