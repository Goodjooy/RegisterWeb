package com.jacky.register.models.database.group;

import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.database.users.Administer;
import com.jacky.register.models.database.users.Student;

import javax.persistence.*;
import java.util.Set;

/**
 * 部门实体类
 */
@Entity
public class GroupDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer ID;

    @Column(unique = true,nullable = false,length = 32)
    String name;

    @Column(nullable = false,length = 225)
    String information;

    @ManyToMany(mappedBy = "underGroup")
    Set<Student> members;

    @OneToMany(targetEntity = Administer.class,mappedBy = "groupIn")
    Set<Administer>administers;

    @OneToMany(targetEntity = Questionable.class)
    public
    Set<Questionable>questions;


}
