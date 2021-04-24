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
            public
    Long id;

    @Column(nullable = false)
            public
    Integer studentID;
    @Transient
            public
    Student student;

    @Column(nullable = false)
            public
    Long examCycleId;
    @Transient
            public
    ExamCycle examCycle;
}
