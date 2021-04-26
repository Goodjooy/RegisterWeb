package com.jacky.register.models.respond.examCycle.data;

import com.jacky.register.models.status.ExamCycleStatus;

import java.io.Serializable;

public class ExamCycleStudentStatus implements Serializable {
    public Long examCycleId;
    public ExamCycleStatus status;
}
