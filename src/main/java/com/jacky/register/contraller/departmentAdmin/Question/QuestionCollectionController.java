package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.dataHandle.Info;
import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.err.BaseException;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.models.respond.question.control.Question;
import com.jacky.register.server.dbServers.qustion.QuestionCollectionServer;
import com.jacky.register.server.dbServers.qustion.QuestionControlServer;
import com.jacky.register.server.modelTransformServers.QuestionCollectionTransformServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    LoggerHandle logger = LoggerHandle.newLogger(QuestionCollectionController.class);

    @PostMapping("/{id:\\d+}")
    public Result<Boolean> uploadCollection(
            @RequestBody QuestionCollectionData data,
            @PathVariable Integer id) {
        var collection = collectionTransformServer.toQuestionCollection(data, id);
        collectionServer.sendQuestionCollect(collection);

        logger.SuccessOperate(
                "Collection Question",
                Info.of(id, "QuestionID")
        );
        return Result.okResult(true);
    }

    @GetMapping("/{id:\\d+}")
    public Result<Question> getCollectionStruct(
            @PathVariable Integer id
    ) {
        var question = controlServer.getQuestionByID(id);
        var questionResult = Question.fromQuestion(question);

        logger.SuccessOperate("Get Question Struct",
                Info.of(id, "QuestionID"));
        return Result.okResult(questionResult);
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleNotSelectTypeItem(BaseException exception, HttpServletRequest request) {
        logger.error(request, exception);
        return exception.toResult();
    }
}
