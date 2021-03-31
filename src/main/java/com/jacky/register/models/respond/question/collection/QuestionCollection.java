package com.jacky.register.models.respond.question.collection;

import com.jacky.register.models.respond.question.control.Question;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionCollection {
    public Question question;
    public List<QuestionCollectionData> questionCollectionData;

    static public QuestionCollection fromQuestion(List<com.jacky.register.models.database.quetionail.collection.QuestionCollection> collections) {
        QuestionCollection collection = new QuestionCollection();
        collection.question = Question.fromQuestion(collections.get(0).question);

        collection.questionCollectionData = collections.stream()
                .map(QuestionCollectionData::fromQuestionCollection)
                .collect(Collectors.toList());

        return collection;
    }
}
