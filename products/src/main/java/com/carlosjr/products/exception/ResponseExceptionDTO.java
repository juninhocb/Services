package com.carlosjr.products.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record ResponseExceptionDTO(
        String uri,
        String message,
        @JsonProperty(value = "date_time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
        Date dateTime) {
}
