package com.jacky.register.models.database.users;

import com.jacky.register.models.database.group.GroupDepartment;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Administer implements Serializable {
    @Id
            @GeneratedValue
    Integer ID;

    @Column(nullable = false,length = 32)
    public
    String name;
    @Column(nullable = false,length = 32)
            public
    String email;
    public String password;

    @ManyToOne(targetEntity = GroupDepartment.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
            public
    GroupDepartment groupIn;
}
