package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.respond.question.collection.QuestionCollection;
import com.jacky.register.server.dbServers.qustion.QuestionDataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question/data")
public class QuestionDataController {

    @Autowired
    QuestionDataServer dataServer;

    @GetMapping("/{id:\\d+}")
    public Result<QuestionCollection>getAllCollection(
            @PathVariable Integer id
    ){
        var result=dataServer.getAllQuestionCollectionData(id);
        // TODO: 2021/3/29 logger cover
        return Result.okResult(result);
    }

}
