package com.jacky.register.server.dbServers.qustion;

import com.jacky.register.models.database.quetionail.collection.QuestionCollectionRepository;
import com.jacky.register.models.respond.question.collection.QuestionCollection;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionDataServer {
    @Autowired
    QuestionCollectionRepository collectionRepository;

    public QuestionCollection getAllQuestionCollectionData(Integer id) {
        var collection = collectionRepository.findByQuestionIDOrderBySubmitAt(id);
        return QuestionCollection.fromQuestion(collection);
    }

    static class QuestionFilterBuilder {

    }
}
