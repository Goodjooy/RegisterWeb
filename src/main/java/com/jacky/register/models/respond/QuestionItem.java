package com.jacky.register.models.respond;

import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionItem implements Serializable {
    public String name;
    public ItemType type;
    public List<QuestionItemSelect> selects;

    public static QuestionItem fromQuestionSubItem(ItemSort sort) {
        QuestionItem item = new QuestionItem();
        var rawItem = sort.item;

        item.name = rawItem.data;
        item.type = rawItem.type;
        item.selects = rawItem.selects == null ? null : rawItem.selects
                .stream().sorted(Comparator.comparing(sort1 -> sort1.sortIndex))
                .map(QuestionItemSelect::fromItemSelect).collect(Collectors.toList());

        return item;
    }
}
