package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.collection.QuestionCollectionRepository;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.models.respond.question.control.Question;
import com.jacky.register.server.dbServers.QuestionCollectionServer;
import com.jacky.register.server.dbServers.QuestionControlServer;
import com.jacky.register.server.modelTransformServers.QuestionCollectionTransformServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 面向用户的收集页面
 * 为用户提供收集表填写功能
 */
@RestController
@RequestMapping("/api/question/collection")
public class QuestionCollectionController {
    @Autowired
    QuestionCollectionServer collectionServer;
    @Autowired
    QuestionControlServer controlServer;
    @Autowired
    QuestionCollectionTransformServer collectionTransformServer;

    @PostMapping("/{id:\\d+}")
    public Result<Boolean> uploadCollection(
            @RequestBody QuestionCollectionData data,
            @PathVariable Integer id){
        var collection=collectionTransformServer.toQuestionCollection(data,id);
        collectionServer.sendQuestionCollect(collection);
        return Result.okResult(true);
    }

    @GetMapping("/{id:\\d+}")
    public Result<Question>getCollectionStruct(
            @PathVariable Integer id
    ){
        // TODO: 2021/3/29 logger cover
        var question=controlServer.getQuestionByID(id);
        var questionResult=Question.fromQuestion(question);
        return Result.okResult(questionResult);
    }
}
