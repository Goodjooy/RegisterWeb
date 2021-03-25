package com.jacky.register.models.database.quetionail.choices;

import com.jacky.register.models.database.quetionail.subItems.QuestionSubItem;

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
    QuestionSubItem item;

    @OneToOne
    @JoinColumn
            public
    SubItemSelect select;
}
