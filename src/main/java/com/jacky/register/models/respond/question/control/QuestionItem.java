package com.jacky.register.models.respond.question.control;

import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionItem implements Serializable {
    public String name;
    public ItemType type;
    public Boolean require;
    public List<QuestionItemSelect> selects;

    public static QuestionItem fromQuestionSubItem(ItemSort sort) {
        QuestionItem item = new QuestionItem();
        var rawItem = sort.item;

        item.name = rawItem.data;
        item.type = rawItem.type;
        item.require=sort.requireFill;
        item.selects = sort.selects == null ? null : sort.selects
                .stream().sorted(Comparator.comparing(sort1 -> sort1.sortIndex))
                .map(QuestionItemSelect::fromItemSelect).collect(Collectors.toList());

        return item;
    }

}
