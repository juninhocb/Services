package com.carlosjr.products.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity getGenericException(Exception ex, HttpServletRequest request){
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO(
                request.getRequestURI(), ex.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseExceptionDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFoundException(Exception ex, HttpServletRequest request){
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO(
                request.getRequestURI(), ex.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseExceptionDTO);
    }



}
