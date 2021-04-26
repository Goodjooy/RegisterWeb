package com.jacky.register.models.status;

public enum ExamStatus {
    ALL,

    REGISTER,
    ASSESS,
    PASS,
    FAILURE,

    ADMIN_SET;

    public boolean canBeContinue(){
        return this==PASS || this==ADMIN_SET;
    }
}
