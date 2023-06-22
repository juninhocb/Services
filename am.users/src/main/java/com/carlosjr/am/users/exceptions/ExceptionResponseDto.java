package com.carlosjr.am.users.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
@Builder
public record ExceptionResponseDto(String timestamp, String message, String cause, @JsonProperty("class_name") String className) {
}
