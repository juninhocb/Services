package com.carlosjr.am.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build()
        );
    }

}
