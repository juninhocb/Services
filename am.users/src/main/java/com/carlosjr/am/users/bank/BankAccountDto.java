package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BankAccountDto(
        @Null UUID id,
        @Size(min = 2, max = 100)
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z1-9\\s]+$")
        String name,
        @NotNull
        @Positive
        @JsonProperty("account_number")
        Long accountNumber,
        @Null
        Boolean isActive,
        BigDecimal amount,
        @NotNull
        UserDto userDto) {
}
