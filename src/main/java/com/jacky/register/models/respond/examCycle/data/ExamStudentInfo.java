package com.jacky.register.models.respond.examCycle.data;

import com.jacky.register.models.status.ExamStatus;

import java.io.Serializable;

public class ExamStudentInfo implements Serializable {
    public Integer id;

    public String name;
    public String studentId;
    public String email;
    public String qq;

    public ExamStatus status;

    public String examWorks;
}
