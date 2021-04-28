package com.jacky.register.models.database.group;

import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.database.users.Administer;
import com.jacky.register.models.database.register.Student;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 部门实体类
 */
@Entity
public class GroupDepartment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public
    Integer ID;

    @Column(unique = true,nullable = false,length = 32)
    public String name;

    @Column(nullable = false,length = 225)
    public String information;

    @ManyToMany(mappedBy = "underGroup")
    public Set<Student> members;

    @OneToMany(targetEntity = Administer.class,mappedBy = "groupIn",fetch = FetchType.EAGER)
    public Set<Administer>administers;

    @OneToMany(targetEntity = Questionable.class,fetch = FetchType.EAGER)
    public
    Set<Questionable>questions;

    public static GroupDepartment lambadaDepartment(){
        GroupDepartment department=new GroupDepartment();
        department.ID=-1;
        department.name="nil";
        department.information="nil";

        return department;
    }

}
