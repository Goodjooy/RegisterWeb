package com.jacky.register.contraller;

import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.database.quetionail.ItemType;
import com.jacky.register.models.database.quetionail.Questionable;
import com.jacky.register.models.respond.Question;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.QuestionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
    @Autowired
    QuestionServer server;
    @Autowired
    DepartmentServer departmentServer;

    LoggerHandle logger =LoggerHandle.newLogger(QuestionController.class);


    @GetMapping("/question")
    public Result<Question> getQuestion(
            @RequestParam("id") Integer id
    ) {
        // TODO: 2021/3/25 check department has target question
        var question = server.getQuestionByID(id);
        var result = Question.fromQuestion(question);

        // TODO: 2021/3/25 logger cover
        return Result.okResult(result);
    }

    @PostMapping("/question")
    public Result<Integer> newQuestion(
            @RequestParam("name") String QuestionName,
            @RequestParam(value = "information", defaultValue = "这是一个新的问卷") String information
    ) {
        // TODO: 2021/3/25 save to department base on the auth admin
        var department = departmentServer.getDepartmentByID(1);

        var question = server.newQuestion(department, QuestionName, information);

        return Result.okResult(question.ID);
    }

    @PostMapping("/item")
    public Result<Integer> addQuestionItem(
            @RequestParam("id") Integer id,
            @RequestParam("information") String information,
            @RequestParam("type") ItemType type
    ) {
        // TODO: 2021/3/25 check the question is under department

        var item = server.newQuestionItem(information, type);
        var itemSort = server.addQuestionItem(server.getQuestionByID(id), item);

        return Result.okResult(itemSort.sortIndex);
    }

    @PutMapping("/item")
    public Result<Integer> updateQuestionItem(
            @RequestParam("qid") Integer id,
            @RequestParam("iid") Integer itemId,
            @RequestParam("information") String information,
            @RequestParam("type") ItemType type

    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        // TODO: 2021/3/25 update item :change information/type
        return null;
    }

    @DeleteMapping("/item")
    public Result<Boolean> removeQuestionItem(
            @RequestParam("qid") Integer id,
            @RequestParam("iid") Integer itemId
    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        var question = server.getQuestionByID(id);
        server.removeItem(question, server.getItemSortByID(question, itemId));

        // TODO: 2021/3/25 logger

        return Result.okResult(true);
    }

    @PostMapping("/select")
    public Result<Integer> addSelectItem(
            @RequestParam("qid") Integer id,
            @RequestParam("iid") Integer itemId,
            @RequestParam("name") String name,
            @RequestParam(value = "userInsert", defaultValue = "false") boolean userInsert
    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        var question = server.getQuestionByID(id);
        var select = server.addItemSelect(server.getItemSortByID(question, itemId).item, name, userInsert);

        // TODO: 2021/3/25 logger
        return Result.okResult(select.sortIndex);
    }

    @DeleteMapping("/select")
    public Result<Boolean> removeSelectItem(
            @RequestParam("qid") Integer id,
            @RequestParam("iid") Integer itemId,
            @RequestParam("sid") Integer SelectID
    ) {
        // TODO: 2021/3/25  check question below the department; check item below question
        var question = server.getQuestionByID(id);
        var ItemSort = server.getItemSortByID(question, itemId);
        var SelectSort = server.getItemSelectSortByID(ItemSort, SelectID);

        server.removeSelectItem(ItemSort, SelectSort);
        // TODO: 2021/3/25 logger
        return Result.okResult(true);
    }
}
