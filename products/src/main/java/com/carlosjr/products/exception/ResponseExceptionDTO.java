package com.carlosjr.products.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public record ResponseExceptionDTO(
        String uri,
        String timestamp,
        @JsonProperty(value = "date_time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
        Date dateTime) {
}
