package com.jacky.register.models.respond.examCycle.operate;

import java.io.Serializable;
import java.util.List;

public class ExamCycleRespond implements Serializable {
    public Long id;

    public Integer departmentId;
    public String departName;

    public Boolean availableRegister;
    // TODO: 2021/4/26 is done?

    public String name;

    public RegisterRespond register;

    public List<ExamRespond> exams;
    public List<StudentRespond> students;
}
