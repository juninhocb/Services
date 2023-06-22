package com.carlosjr.am.users.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserDto(
        @NotNull
        @Positive
        @JsonProperty("group_id")
        Long groupId,
        @NotBlank
        @Size(min = 3, max = 50)
        @Pattern(regexp = "^[a-zA-Z\\s]+$")
        @JsonProperty("full_name")
        String fullName,
        @NotBlank
        @Size(min = 3, max = 50)
        String username,
        @NotBlank
        @Size(min = 4, max = 50)
        String password,
        @NotBlank
        @Email
        String email) {
}
