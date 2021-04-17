package com.jacky.register.models.request.register.examCycle;

import org.springframework.web.bind.annotation.PutMapping;

public class UpdateExam {
    public Long examId;
    public ExamData data;
}
