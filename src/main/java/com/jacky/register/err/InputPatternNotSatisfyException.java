package com.jacky.register.err;

import java.lang.reflect.Field;

public class InputPatternNotSatisfyException extends BaseException{
    private static final int errorCode=200;

    public InputPatternNotSatisfyException(Class<?>clazz,Field field,String message){
        super(errorCode,
                String.format(
                        "Target Input Data<%s> Field<%s> Has Noexcept Data | %s",
                        clazz.getName(),field.getName(),message
                ));
    }
}
