package com.jacky.register.err.register.requireNotSatisfy;

import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.Term.Exam;

public class HandOnWorksOverTimeException extends BaseException {
    final static int errorCode=205;

    public HandOnWorksOverTimeException(Exam exam){
        super(
                errorCode,
                String.format(
                        "Exam<`%s`> Start At %s ,End At %s",
                        exam.name,exam.startAt,exam.endAt
                )
        );
    }
}
