package com.jacky.register.err;

public class RowNotFoundException extends RuntimeException{
    public RowNotFoundException(String message){
        super(message);
    }
}
