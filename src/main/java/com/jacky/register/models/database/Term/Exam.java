package com.jacky.register.models.database.Term;

import com.jacky.register.models.database.register.RegisterQuestion;
import com.jacky.register.models.database.register.Student;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

//考核轮
@Entity
public class Exam {
    @Id
    @GeneratedValue
            public
    Long id;
    //所在的考核周期

    @Column(nullable = false)
    public
    Long examCycleID;


    //本轮学生
    @Transient
            public
    Set<Student>termStudents;

    //本轮考核文件
    public
    String requireFile;

    //start at
    @Column(nullable = false)
            public
    LocalDate startAt;

    @Column(nullable = false)
            public
    LocalDate endAt;


}
