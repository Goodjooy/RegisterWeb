package com.jacky.register.models.request.register.examCycle;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ExamData {
    //考核周期id
    public
    Long cycleID;
    //考核名称
    public
    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate startAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate endAt;
}
