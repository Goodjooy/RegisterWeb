package com.jacky.register.models.database.users;

import com.jacky.register.models.database.group.GroupDepartment;

import javax.persistence.*;

@Entity
public class Administer {
    @Id
    Integer ID;

    String name;
    String stdID;
    String email;
    String password;

    @ManyToOne(targetEntity = GroupDepartment.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    GroupDepartment groupIn;
}
