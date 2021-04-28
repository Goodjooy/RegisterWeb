package com.jacky.register.err.register.requireNotSatisfy;

import com.jacky.register.err.BaseException;

public class ExamCycleClosedException extends BaseException {
    static final int errorCode=207;

    public ExamCycleClosedException(Long id){
        super(
                errorCode,
                String.format("ExamCycle<ID: %s> Closed",id)
        );
    }

}
