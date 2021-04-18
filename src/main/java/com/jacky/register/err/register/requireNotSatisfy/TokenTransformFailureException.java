package com.jacky.register.err.register.requireNotSatisfy;

import com.jacky.register.err.BaseException;

public class TokenTransformFailureException extends BaseException {
    static final int errorCode=204;

    public TokenTransformFailureException(Throwable e){
        super(errorCode,e);
    }
}
