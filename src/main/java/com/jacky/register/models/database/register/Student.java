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
    Integer id;

    @Column(nullable = false, length = 10, unique = true)
    String name;
    @Column(nullable = false, unique = true, length = 10)
    String stdID;

    @Column(nullable = false, unique = true)
    String email;
    @Column(length = 20, unique = true)
    String phone;
    @Column(length = 20, unique = true)
    String qqID;

    //many to many

    @ManyToMany()
    @JoinTable(
            name = "student_group",
            joinColumns = @JoinColumn(name = "std_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    Set<GroupDepartment> underGroup;

    @Transient
    //学生所在考核轮/不同部门/不同轮
    Set<Exam> studentExam;
}
