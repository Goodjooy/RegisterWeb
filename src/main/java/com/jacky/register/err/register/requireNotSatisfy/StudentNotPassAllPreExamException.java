package com.jacky.register.err.register.requireNotSatisfy;

import com.jacky.register.err.BaseException;

import javax.persistence.criteria.CriteriaBuilder;

public class StudentNotPassAllPreExamException extends BaseException {
    static final int errorCode=203;

    public StudentNotPassAllPreExamException(Integer id){
        super(
                errorCode,
                String.format(
                        "Student<ID:%s> not Pass All Pre Exam",
                        id
                )
        );
    }
}
