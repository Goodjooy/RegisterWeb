package com.jacky.register.models.database.quetionail.subItems;

import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.choices.SelectSort;

import javax.persistence.*;
import java.util.Set;

/*
 * 问卷的子项
 *
 * 多选问题
 *
 */
@Entity
public class QuestionSubItem {
    @Id
    @GeneratedValue
    public
    Integer id;
    public
    String data;
    //type
    public
    ItemType type;

    //selects
    @OneToMany(fetch = FetchType.LAZY)
    public
    Set<SelectSort> selects;

}
