package com.jacky.register.models.status;

public enum ExamCycleStatus {
    ALL,

    REGISTER,
    PASS,
    REJECT,

    ADMIN_SET;

    public boolean canBeContinue(){
        return this==PASS || this==ADMIN_SET;
    }
}
