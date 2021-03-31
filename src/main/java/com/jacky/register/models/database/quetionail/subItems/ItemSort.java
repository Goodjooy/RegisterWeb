package com.jacky.register.models.database.quetionail.subItems;

import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.database.quetionail.choices.SelectSort;

import javax.persistence.*;
import java.util.List;

/**
 * 保存元素排序信息
 */
@Entity
public class ItemSort {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public
    Integer id;
    @Column(nullable = false)
    public
    Integer sortIndex;

    @ManyToOne()
    @JoinColumn()
    public
    Questionable question;

    @OneToOne()
    @JoinColumn()
    public
    QuestionSubItem item;

    @Column(nullable = false)
    public
    Boolean requireFill;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("sort_index")
    public List<SelectSort>selects;

}
