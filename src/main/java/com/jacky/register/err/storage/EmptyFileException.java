package com.jacky.register.err.storage;

import com.jacky.register.err.BaseException;

public class EmptyFileException extends BaseException {
    private static final int errorCode=206;

    public EmptyFileException(String fileName){
        super(errorCode,
                String.format("Upload File<Name:`%s`> is Empty",fileName)
                );
    }
}
