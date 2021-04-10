package com.jacky.register.models.request.register.examCycle;

import java.time.LocalDate;

//新建考核
public class CreateExam {
    //考核周期id
    public
    Long cycleID;
    //考核名称
    public
    String name;

    public LocalDate startAt;
    public LocalDate endAt;
}
