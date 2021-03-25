package com.jacky.register.models.database.users;

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
    //todo term data
    //Set<Term> underTerm;
}
