package com.jacky.register.models.database.quetionail;

import com.jacky.register.models.database.quetionail.subItems.ItemSort;
import com.jacky.register.models.database.quetionail.subItems.QuestionSubItem;

import javax.persistence.*;

public class TabContain {
    @Id
    Integer id;
    @Column(nullable = false)
    String data;
    @Column(nullable = false)
    Integer index;

    //in-data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    ItemSort target;
}
