package com.jacky.register.models.respond;

import com.jacky.register.models.database.quetionail.choices.SelectSort;

import java.io.Serializable;

public class QuestionItemSelect implements Serializable {
    public String information;
    public boolean userInsert;

    public static QuestionItemSelect fromItemSelect(SelectSort sort) {
        QuestionItemSelect select = new QuestionItemSelect();
        var temp = sort.select;

        select.information = temp.information;
        select.userInsert = temp.userInsert;

        return select;
    }
}
