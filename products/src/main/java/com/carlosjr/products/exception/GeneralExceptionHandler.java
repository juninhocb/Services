package com.carlosjr.products.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseExceptionDTO> getGenericException(Exception ex, HttpServletRequest request){
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO(
                request.getRequestURI(), ex.getMessage(), new Date());
        System.out.println("get class: " + ex.getClass());  //dev purpose...
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseExceptionDTO);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseExceptionDTO> methodArgumentMismatchHandler(MethodArgumentTypeMismatchException ex, HttpServletRequest request){
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO(
                request.getRequestURI(), ex.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseExceptionDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> resourceNotFoundException(Exception ex, HttpServletRequest request){
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO(
                request.getRequestURI(), ex.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseExceptionDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseExceptionDTO> methodArgumentNotValidException(Exception ex, HttpServletRequest request){
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO(
                request.getRequestURI(), ex.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseExceptionDTO);
    }

}
