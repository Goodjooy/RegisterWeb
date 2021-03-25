package com.jacky.register.models.database.quetionail.choices;

import com.jacky.register.models.database.quetionail.subItems.QuestionSubItem;

import javax.persistence.*;

/**
 * 问卷问题的选项表
 */
@Entity
public class SubItemSelect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
            public
    Integer id;
    @Column(nullable = false, length = 32)
            public
    String information;

    //@ManyToOne
    //@JoinColumn(nullable = false)
    //QuestionSubItem item;
    @Column(nullable = false)
            public
    Boolean userInsert;


}
