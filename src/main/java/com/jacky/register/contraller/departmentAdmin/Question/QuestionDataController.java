package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.dataHandle.Info;
import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.err.BaseException;
import com.jacky.register.models.respond.question.collection.QuestionCollection;
import com.jacky.register.server.dbServers.qustion.QuestionDataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/question/data")
public class QuestionDataController {

    @Autowired
    QuestionDataServer dataServer;

    LoggerHandle logger =LoggerHandle.newLogger(QuestionDataController.class);

    @GetMapping("/{id:\\d+}")
    public Result<QuestionCollection>getAllCollection(
            @PathVariable Integer id
    ){
        var result=dataServer.getAllQuestionCollectionData(id);

        logger.SuccessOperate("Get All Question Collection",
                Info.of(id,"QuestionID"));
        return Result.okResult(result);
    }
    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleNotSelectTypeItem(BaseException exception, HttpServletRequest request) {
        logger.error(request, exception);
        return exception.toResult();
    }
}
