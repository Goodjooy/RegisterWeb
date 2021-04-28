package com.jacky.register.models.database.register.registerCollection;

import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.register.Student;

import javax.persistence.*;

//考核终了收集表
@Entity
public class ExamFinalCollection {
    @Id
    @GeneratedValue
    public
    Long id;

    //学生id；
    @Column(nullable = false, unique = true, length = 20)
    public
    Integer studentID;
    @Transient
    Student student;

    //考核轮ID
    @Column(nullable = false)
    public
    Long examID;
    @Transient
    Exam exam;

    //考核提交文件路径
    @Column(nullable = false, length = 128)
    public
    String examFile;

    static public ExamFinalCollection nullWorks(Integer student, Long exam) {
        var result = new ExamFinalCollection();

        result.examFile = null;
        result.studentID = student;
        result.examID = exam;
        result.id = -1L;

        return result;
    }

}
