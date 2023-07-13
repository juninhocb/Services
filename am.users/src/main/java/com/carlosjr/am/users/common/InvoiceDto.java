package com.carlosjr.am.users.common;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;
@Builder
public record InvoiceDto(
        //managed by new other service
        @NotNull
        @JsonProperty("invoice_id")
        UUID invoiceId,
        @JsonProperty("account_number")
        Long accountNumber,
        BigDecimal amount

) {
}
