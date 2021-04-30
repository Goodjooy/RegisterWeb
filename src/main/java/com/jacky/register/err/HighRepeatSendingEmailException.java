package com.jacky.register.err;

import com.jacky.register.err.BaseException;

public class HighRepeatSendingEmailException extends BaseException {
    final static int errorCode= 1;
    public HighRepeatSendingEmailException(){
        super(
                errorCode,
                "Send Email To Same Address Too Frequently<less 60S>"
        );
    }
}
