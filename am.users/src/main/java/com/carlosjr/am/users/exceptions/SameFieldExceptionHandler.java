package com.carlosjr.am.users.exceptions;

public class SameFieldExceptionHandler extends RuntimeException{
    public SameFieldExceptionHandler(String message){
        super(message);
    }
}
