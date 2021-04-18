package com.jacky.register.err.register.notFound;

import com.jacky.register.err.BaseException;

public class StudentNotFoundException extends BaseException {
    final  static int errorCode=406;

    public StudentNotFoundException(int id){
        super(
                errorCode,
                String.format("Student<ID:%s> Not Found",id)
        );
    }
}
