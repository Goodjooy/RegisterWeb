package com.jacky.register.models.database.register.registerCollection;

import com.jacky.register.models.status.ExamStatus;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.register.Student;

import javax.persistence.*;

@Entity
//学生和考核轮链接
//每个考核轮发布后，通过上一轮考核即可
//学生信息不变，软链接即可
public class StudentExamLink {

    @Id
    @GeneratedValue
    public
    Long id;

    //学生的id
    @Column(nullable = false)
    public
    Integer studentID;
    @Transient
    public
    Student student;

    //考核轮ID
    @Column(nullable = false)
    public
    Long examId;
    @Transient
    public
    Exam exam;
    //考核状态
    @Column(nullable = false)
    public
    ExamStatus status;
}
