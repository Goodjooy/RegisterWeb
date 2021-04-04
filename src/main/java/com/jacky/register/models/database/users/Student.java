package com.jacky.register.models.database.users;

import com.jacky.register.models.database.Term.Term;
import com.jacky.register.models.database.group.GroupDepartment;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(nullable = false, length = 10)
    String name;
    @Column(nullable = false, length = 10)
    String stdID;

    @Column(nullable = false)
    String email;
    @Column(length = 20)
    String phone;
    @Column(length = 20)
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
    Set<Term>studentTerm;
}
