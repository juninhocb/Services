package com.carlosjr.am.users.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> generalExceptionHandler(Exception ex){
        log.error("[ GeneralExceptionHandler ] Exception not handled: " + ex.getClass());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex){
        log.error("[ GeneralExceptionHandler ] IllegalArgumentExceptionHandler ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build()
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> argumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        log.error("[ GeneralExceptionHandler ] argumentNotValidExceptionHandler ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build()
        );
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> dataViolationExceptionHandler(DataIntegrityViolationException ex){
        log.error("[ GeneralExceptionHandler ] dataViolationExceptionHandler ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build()
        );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        log.error("[ GeneralExceptionHandler ] resourceNotFoundExceptionHandler ");
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

    @ExceptionHandler(SameFieldExceptionHandler.class)
    public ResponseEntity<ExceptionResponseDto> sameFieldExceptionHandler(SameFieldExceptionHandler ex){
        log.error("[ GeneralExceptionHandler ] sameFieldExceptionHandler ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build()
        );
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponseDto> methodNotSupportException(HttpRequestMethodNotSupportedException ex){
        log.error("[ GeneralExceptionHandler ] methodNotSupportException ");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                ExceptionResponseDto
                        .builder()
                        .cause(ex.getCause() == null ? null : ex.getCause().toString())
                        .message(ex.getMessage())
                        .className(ex.getClass().toString())
                        .timestamp(new Date().toString())
                        .build()
        );
    }

    @ExceptionHandler(AccessTokenExpirationException.class)
    public ResponseEntity<ExceptionResponseDto> notLoggedInHandler(AccessTokenExpirationException ex){
        log.error("[ GeneralExceptionHandler ] notLoggedInHandler ");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
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
