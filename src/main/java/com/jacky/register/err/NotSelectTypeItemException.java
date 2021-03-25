package com.jacky.register.err;

public class NotSelectTypeItemException extends RuntimeException{
    public NotSelectTypeItemException(String message){
        super(message);
    }
    public NotSelectTypeItemException(String message,Throwable t){
        super(message,t);
    }
}
