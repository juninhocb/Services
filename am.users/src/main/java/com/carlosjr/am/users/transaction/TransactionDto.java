package com.carlosjr.am.users.transaction;


import com.carlosjr.am.users.bank.BankAccountDto;
import com.carlosjr.am.users.user.UserDto;
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
        UserDto userDto,
        @NotNull
        BankAccountDto bankAccountDto,
        @NotNull
        BigDecimal amount,
        @NotNull
        TransactionState state,
        @Null
        @JsonProperty("created_time")
        LocalDateTime createdTime

    ) {
}
