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
        log.info("exception not handled: " + ex.getClass());
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


}
