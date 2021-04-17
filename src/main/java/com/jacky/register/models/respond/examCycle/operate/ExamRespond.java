package com.jacky.register.models.respond.examCycle.operate;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class ExamRespond implements Serializable {
    public Long id;
    public String name;

    public String examFileName;

    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    public LocalDate startAt;
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    public LocalDate endAt;


}
