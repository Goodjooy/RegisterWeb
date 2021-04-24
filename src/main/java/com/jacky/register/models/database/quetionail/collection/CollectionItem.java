package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.subItems.ItemSort;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CollectionItem {
    @Id
    @GeneratedValue
    Integer id;

    @Column(length = 128, nullable = false)
    public String data;

    @ManyToOne
    @JoinColumn
    public ItemSort item;

    @OneToMany
    public Set<CollectionItemSelect> selects;
}
