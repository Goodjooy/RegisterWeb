package com.jacky.register.models.respond.examCycle.operate;

import com.jacky.register.models.ExamStatus;

import java.io.Serializable;

public class StudentStatusRespond implements Serializable {
    //本信息所在的考核
    public Long ExamId;
    //状态
    public ExamStatus status;
}
