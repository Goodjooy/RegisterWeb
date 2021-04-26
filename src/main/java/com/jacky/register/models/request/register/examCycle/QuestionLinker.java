package com.jacky.register.models.request.register.examCycle;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;


public class QuestionLinker implements Serializable {
    //绑定的问题id
    public Integer questionID;

    //问卷信息绑定
    public Integer studentIDItemID;
    public Integer studentNameItemID;
    public Integer emailItemID;
    public Integer qqItemID;
    public Integer phoneItemID;

    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate endAt;
}
