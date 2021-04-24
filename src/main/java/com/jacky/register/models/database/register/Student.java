package com.jacky.register.models.database.register;

import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.group.GroupDepartment;

import javax.persistence.*;
import java.util.Set;

@Entity
//学生信息表
//学生报名
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
            public
    Integer id;

    @Column(nullable = false, length = 10, unique = true)
            public
    String name;
    @Column(nullable = false, unique = true, length = 10)
            public
    String stuID;

    @Column(nullable = false, unique = true)
            public
    String email;
    @Column(length = 20, unique = true)
            public
    String phone;
    @Column(length = 20, unique = true)
            public
    String qqID;

    //many to many

    @ManyToMany()
    @JoinTable(
            name = "student_group",
            joinColumns = @JoinColumn(name = "std_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
public
    Set<GroupDepartment> underGroup;

    @Transient
    //学生所在考核轮/不同部门/不同轮
            public
    Set<Exam> studentExam;
}
