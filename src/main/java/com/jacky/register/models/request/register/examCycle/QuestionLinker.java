package com.jacky.register.models.request.register.examCycle;

import java.io.Serializable;


public class QuestionLinker implements Serializable {
    //绑定的问题id
    public Integer questionId;

    //问卷信息绑定
    public Integer studentItemID;
    public Integer studentNameItemID;
    public Integer emailItemID;
    public Integer qqItemID;
}
