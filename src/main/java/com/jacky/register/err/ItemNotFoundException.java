package com.jacky.register.err;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(Integer id){
        super("Item not found : "+id);
    }
}
