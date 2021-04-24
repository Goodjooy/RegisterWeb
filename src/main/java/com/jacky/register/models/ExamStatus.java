package com.jacky.register.models;

public enum ExamStatus {
    REGISTER,
    ASSESS,
    PASS,
    FAILURE,

    ADMIN_SET;

    public boolean canBeContinue(){
        return this==PASS || this==ADMIN_SET;
    }
}
