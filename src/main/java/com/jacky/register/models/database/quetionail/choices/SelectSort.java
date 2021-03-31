package com.jacky.register.models.database.quetionail.choices;

import com.jacky.register.models.database.quetionail.subItems.ItemSort;

import javax.persistence.*;

@Entity
public class SelectSort {
    @Id
    @GeneratedValue
    Integer id;
    @Column(nullable = false)
    public
    Integer sortIndex;

    @ManyToOne
    @JoinColumn
    public
    ItemSort item;

    @OneToOne
    @JoinColumn
    public
    SubItemSelect select;
}
