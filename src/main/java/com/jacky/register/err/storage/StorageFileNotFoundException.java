package com.jacky.register.err.storage;

import com.jacky.register.err.BaseException;

public class StorageFileNotFoundException extends BaseException {
    static final int errorCode=408;

    public StorageFileNotFoundException(String filePath){
        super(errorCode,
                String.format("Storage File Name <%s> Not Found",filePath));
    }
}
