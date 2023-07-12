package com.carlosjr.am.users.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserTokenDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("expires_in")
        Long expiresIn) {
}
