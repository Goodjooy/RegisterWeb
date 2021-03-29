package com.jacky.register.models.database.quetionail;

import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Questionable {
    @Id
    @GeneratedValue
    public
    Integer ID;

    @Column(nullable = false,length = 32)
    public
    String title;

    @Column(nullable = false)
    public
    String information;

    @OneToMany
    public
    Set<ItemSort> items;

    @ManyToOne
    @JoinColumn
    public
    GroupDepartment department;

    @Column(nullable = false)
    public
    Boolean publish;
}
