package com.jacky.register.models.respond.examCycle.data;

import com.jacky.register.models.status.ExamStatus;

import java.io.Serializable;

public class ExamStudentStatus implements Serializable {
    public Long examId;
    public ExamStatus status;
}
