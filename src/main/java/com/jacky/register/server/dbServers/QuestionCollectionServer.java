package com.jacky.register.server.dbServers;

import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItemRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItemSelectRepository;
import com.jacky.register.models.database.quetionail.collection.QuestionCollectionRepository;
import com.jacky.register.models.respond.question.collection.QuestionCollection;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
public class QuestionCollectionServer {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionCollectionRepository collectionRepository;
    @Autowired
    CollectionItemRepository itemRepository;
    @Autowired
    CollectionItemSelectRepository selectRepository;

    public void sendQuestionCollect(QuestionCollectionData collection){
        var questionCollection=collection.toQuestionCollection(questionRepository);

        var result=questionCollection.items.stream()
                .peek(collectionItem -> {
                    var temp=collectionItem.selects.stream()
                            .peek(collectionItemSelect -> selectRepository.save(collectionItemSelect))
                            .collect(Collectors.toSet());
                    itemRepository.save(collectionItem);
                }).collect(Collectors.toList());
        collectionRepository.save(questionCollection);
    }


}
