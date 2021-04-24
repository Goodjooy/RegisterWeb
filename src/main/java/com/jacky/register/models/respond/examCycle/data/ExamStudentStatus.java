package com.jacky.register.models.respond.examCycle.data;

import com.jacky.register.models.ExamStatus;
import org.apache.juli.logging.Log;

import java.io.Serializable;

public class ExamStudentStatus implements Serializable {
    public Long examId;
    public ExamStatus status;
}
