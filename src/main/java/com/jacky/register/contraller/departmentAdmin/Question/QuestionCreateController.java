package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.err.BaseException;
import com.jacky.register.models.request.quesion.item.ItemCreate;
import com.jacky.register.models.request.quesion.item.ItemUpdate;
import com.jacky.register.models.request.quesion.itemSelect.ItemSelectCreate;
import com.jacky.register.models.request.quesion.itemSelect.ItemSelectUpdate;
import com.jacky.register.models.respond.question.control.Question;
import com.jacky.register.models.respond.question.control.QuestionItem;
import com.jacky.register.models.respond.question.control.QuestionItemSelect;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.QuestionControlServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question/control")
@CrossOrigin
public class QuestionCreateController {
    @Autowired
    QuestionControlServer server;
    @Autowired
    DepartmentServer departmentServer;

    LoggerHandle logger = LoggerHandle.newLogger(QuestionCreateController.class);


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
        var department = departmentServer.getFirstDepartment();

        var question = server.newQuestion(department, QuestionName, information);

        return Result.okResult(question.ID);
    }

    @PostMapping("/item")
    public Result<Integer> addQuestionItem(
            @RequestBody ItemCreate itemCreate
    ) {
        // TODO: 2021/3/25 check the question is under department

        var item = server.newQuestionItem(itemCreate);
        var itemSort = server.addQuestionItem(server.getQuestionByID(itemCreate.questionID), item,itemCreate.data.require);

        return Result.okResult(itemSort.sortIndex);
    }

    @PutMapping("/item")
    public Result<Integer> updateQuestionItem(
            @RequestBody ItemUpdate itemUpdate
    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        // TODO: 2021/3/25 update item :change information/type
        var sort = server.updateItem(itemUpdate);
        return Result.okResult(sort.sortIndex);
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
            @RequestBody ItemSelectCreate selectCreate
    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        var select = server.addItemSelect(selectCreate);

        // TODO: 2021/3/25 logger
        return Result.okResult(select.sortIndex);
    }

    @PutMapping("/select")
    public Result<Integer> updateSelectItem(
            @RequestBody ItemSelectUpdate selectUpdate
    ) {
        var select = server.updateSelect(selectUpdate);
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

    @PostMapping("/fullQuestion")
    public Result<Boolean> publicFull(
            @RequestBody Question question
    ) {
        var department = departmentServer.getFirstDepartment();
        var questionCreate = server.newQuestion(department, question.name, question.information);
        for (QuestionItem item :
                question.items) {
            var newItem = server.newQuestionItem(item.name, item.type);
            var newITemSort = server.addQuestionItem(questionCreate, newItem,item.require);

            for (QuestionItemSelect select :
                    item.selects) {
                server.addItemSelect(newITemSort, select.information, select.userInsert);
            }
        }
        server.publicQuestion(questionCreate.ID);
        return Result.okResult(true);
    }

    @PostMapping("/public")
    public Result<Boolean> publicQuestion(
            @RequestParam("id") Integer id
    ) {
        // TODO: 2021/3/28 visitable problem
        server.publicQuestion(id);
        return Result.okResult(true);
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleNotSelectTypeItem(BaseException exception) {
        logger.error(exception);
        return exception.toResult();
    }
}
