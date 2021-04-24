package com.jacky.register.err.storage;

import com.jacky.register.err.BaseException;

public class StorageOperateFailureException extends BaseException {
    public StorageOperateFailureException(String msg,Throwable cause){
        super(101,msg,cause);
    }
}
