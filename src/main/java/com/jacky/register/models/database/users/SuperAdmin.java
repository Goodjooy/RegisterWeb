package com.jacky.register.models.database.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class SuperAdmin implements Serializable {
    @Id
            @GeneratedValue
    Integer id;

    @Column(nullable = false,length = 128,unique = true)
    public
    String email;

    @Column(nullable = false,length = 64)
    public
    String password;

    @Column(nullable = false,length = 32)
    public
    String name;
}
