package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.user.User;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BankAccountDto(
        String name,
        Long accountNumber,
        Boolean isActive,
        BigDecimal amount,
        User user) {
}
