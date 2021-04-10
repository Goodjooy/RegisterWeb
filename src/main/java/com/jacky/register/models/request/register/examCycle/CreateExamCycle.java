package com.jacky.register.models.request.register.examCycle;

import java.io.Serializable;

/**
 * 新建测试周期
 *
 * 本次考核周期名称
 * 本次考核周期报名表
 */
public class CreateExamCycle implements Serializable {
    public String name;
    // 考核周期问卷连接器
    public QuestionLinker linker;
}
