package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.choices.SelectSort;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
@Entity
public class CollectionItemSelect {

    @Id
    @GeneratedValue
    Integer id;

    @ManyToOne
    @JoinColumn
    SelectSort select;

    @Column(length = 32)
    String value;
}
