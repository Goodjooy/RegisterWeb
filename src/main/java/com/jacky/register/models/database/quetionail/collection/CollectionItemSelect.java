package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.choices.SelectSort;

import javax.persistence.*;

@Entity
public class CollectionItemSelect {

    @Id
    @GeneratedValue
    Integer id;

    @ManyToOne
    @JoinColumn
    public
    SelectSort select;

    @Column(length = 32)
    public
    String value;
}
