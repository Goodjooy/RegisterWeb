package com.jacky.register.err.register.notFound;

import com.jacky.register.err.BaseException;

public class RegisterQuestionNotFoundException extends BaseException {
    private static final int errorCode=405;

    public RegisterQuestionNotFoundException(Long id){
        super(errorCode,
                String.format("RegisterQuestion<%s> Not Found",id));
    }
}
