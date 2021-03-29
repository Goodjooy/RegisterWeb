package com.jacky.register.server.dbServers;

import com.jacky.register.models.database.quetionail.collection.QuestionCollectionRepository;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionDataServer {
    @Autowired
    QuestionCollectionRepository collectionRepository;

    public List<QuestionCollectionData> getAllQuestionCollectionData(Integer id) {
        var collection = collectionRepository.findByQuestionIDOrderBySubmitAt(id);
        return collection.stream()
                .map(QuestionCollectionData::fromQuestionCollection)
                .collect(Collectors.toList());
    }

    static class QuestionFilterBuilder {

    }
}
