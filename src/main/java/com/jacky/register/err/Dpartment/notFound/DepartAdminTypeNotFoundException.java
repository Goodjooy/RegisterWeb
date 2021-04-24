package com.jacky.register.err.Dpartment.notFound;

import com.jacky.register.err.BaseException;

public class DepartAdminTypeNotFoundException extends BaseException {
    private final static int errorCode=409;
    public DepartAdminTypeNotFoundException(Class<?>targetClazz){
        super(errorCode,
                String.format("Clazz<%s> not a support Administer Entity",targetClazz.getName()));
    }
}
