package com.jacky.register.models.database.register.registerCollection;

import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.register.Student;

import javax.persistence.*;

/**
 * 考核开始时报名
 */
@Entity
public class StudentExamCycleLink {
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long studentID;
    @Transient
    Student student;

    @Column(nullable = false)
    Long examCycleID;
    @Transient
    ExamCycle examCycle;
}
