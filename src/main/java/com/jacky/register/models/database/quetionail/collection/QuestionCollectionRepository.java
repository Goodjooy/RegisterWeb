package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.Questionable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionCollectionRepository extends JpaRepository<QuestionCollection,Integer> {
    List<QuestionCollection>findByQuestionOrderBySubmitAt(Questionable question);


}
