package com.jacky.register.models.database.users;

import com.jacky.register.models.database.group.GroupDepartment;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Administer implements Serializable {
    @Id
            @GeneratedValue
    public
    Integer ID;

    @Column(nullable = false,length = 32)
    public
    String name;
    @Column(nullable = false,length = 32)
            public
    String email;
    @Column(length = 11)
    public String studentId;
    @Column(length = 64)
    public String password;

    @ManyToOne(targetEntity = GroupDepartment.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
            public
    GroupDepartment groupIn;
}
