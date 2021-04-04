package com.jacky.register.models.database.register.registerCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
//学生和考核轮链接
public class StudentTermLink {
    public enum ExamStatus{
        REGISTER,
        ASSESS,
        PASS,
        FAILURE
    }

    @Id
    @GeneratedValue
    Long id;

    //学生的id
    @Column(nullable = false)
    Integer studentID;
    //考核轮ID
    @Column(nullable = false)
    Integer termID;

    //考核状态
    @Column(nullable = false)
    ExamStatus status;
}
