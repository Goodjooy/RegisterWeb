package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.subItems.ItemSort;
import com.jacky.register.models.respond.QuestionItem;

import javax.persistence.*;
import java.util.Set;
@Entity
public class CollectionItem {
    @Id
    @GeneratedValue
    Integer id;

    @Column(length = 128,nullable = false)
    String data;

    @ManyToOne
    @JoinColumn
    ItemSort item;

    @OneToMany
    Set<CollectionItemSelect>selects;
}
