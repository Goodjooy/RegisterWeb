package com.jacky.register.models.respond.examCycle.data;

import com.jacky.register.models.ExamStatus;

import java.io.Serializable;
import java.util.List;

public class ExamStudentInfo implements Serializable {
    public Integer id;

    public String name;
    public String studentId;
    public String email;

    public ExamStatus status;

    public String examWorks;
}
