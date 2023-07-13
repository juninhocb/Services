package com.carlosjr.am.users.exceptions;

public class AccessTokenExpirationException extends RuntimeException{
    public AccessTokenExpirationException(String message){
        super(message);
    }
}
