package com.jacky.register.models.respond.question.control;

import com.jacky.register.models.database.quetionail.Questionable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Question implements Serializable {
    public String name;
    public String information;

    public List<QuestionItem> items;

    public static Question fromQuestion(Questionable questionable) {
        Question question = new Question();
        question.name = questionable.title;
        question.information = questionable.information;
        question.items = questionable.items == null ? null : questionable.items
                .stream().sorted(Comparator.comparing(itemSort -> itemSort.sortIndex))
                .map(QuestionItem::fromQuestionSubItem).collect(Collectors.toList());

        return question;
    }
}
