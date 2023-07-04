package com.carlosjr.am.users.transaction;


import com.carlosjr.am.users.bank.BankAccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransactionDto(
        @Null
        UUID id,
        @NotNull
        BankAccountDto bankAccountDto,
        @NotNull
        BigDecimal amount,
        @NotNull
        @JsonProperty("invoice_id")
        UUID invoiceId,
        @Null
        @JsonProperty("created_time")
        LocalDateTime createdTime

    ) {
}
