package com.jacky.register.models.request.register.examCycle;

import java.io.Serializable;
import java.time.LocalDate;

public class ExamCycleData implements Serializable {
    public String name;
    // 考核周期问卷连接器
    public QuestionLinker linker;


}
