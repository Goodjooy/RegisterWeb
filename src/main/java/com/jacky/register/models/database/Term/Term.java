package com.jacky.register.models.database.Term;

import com.jacky.register.models.database.register.RegisterQuestion;
import com.jacky.register.models.database.register.Student;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

//考核轮
@Entity
public class Term {
    @Id
    @GeneratedValue
    Long id;

    //本轮学生
    @Transient
    Set<Student>termStudents;

    //本轮报名问卷
    Long registerQuestionID;
    @Transient
    RegisterQuestion registerQuestion;

    //start at
    @Column(nullable = false)
    LocalDateTime startAt;

    @Column(nullable = false)
    LocalDateTime endAt;


}
