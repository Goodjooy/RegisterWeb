package com.jacky.register.err;

public class RowNotFound extends RuntimeException{
    public  RowNotFound(String message){
        super(message);
    }
}
