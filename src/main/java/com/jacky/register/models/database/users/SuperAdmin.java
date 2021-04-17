package com.jacky.register.models.database.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SuperAdmin {
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
