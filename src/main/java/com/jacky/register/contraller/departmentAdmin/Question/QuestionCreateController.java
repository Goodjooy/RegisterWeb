package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.dataHandle.Info;
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

import javax.servlet.http.HttpServletRequest;

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
        logger.dataAccept(Info.of(id, "QuestionID"));
        // TODO: 2021/3/25 check department has target question
        var question = server.getQuestionByID(id);
        var result = Question.fromQuestion(question);

        logger.SuccessOperate("Get Question By ID",
                Info.of(id, "QuestionID"));
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

        logger.SuccessOperate("New Question",
                Info.of(question.ID, "NewQuestionID"),
                Info.of(QuestionName, "NewQuestionName"),
                Info.of(information, "QuestionInformation"));
        return Result.okResult(question.ID);
    }

    @PostMapping("/item")
    public Result<Integer> addQuestionItem(
            @RequestBody ItemCreate itemCreate
    ) {
        // TODO: 2021/3/25 check the question is under department

        var item = server.newQuestionItem(itemCreate);
        var itemSort = server
                .addQuestionItem(
                        server.getQuestionByID(itemCreate.questionID),
                        item,
                        itemCreate.data.require,
                        itemCreate.data.unique);

        logger.SuccessOperate("Add Item To Question",
                Info.of(itemCreate.questionID, "QuestionID"),
                Info.of(itemSort.sortIndex, "NewItemID"));
        return Result.okResult(itemSort.sortIndex);
    }

    @PutMapping("/item")
    public Result<Integer> updateQuestionItem(
            @RequestBody ItemUpdate itemUpdate
    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        var sort = server.updateItem(itemUpdate);

        logger.SuccessOperate("Update Item In Question",
                Info.of(itemUpdate.questionID, "QuestionID"),
                Info.of(itemUpdate.itemSortID, "ItemSortID"));
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

        logger.SuccessOperate("Remove Item In Question",
                Info.of(id, "QuestionID"),
                Info.of(itemId, "ItemID"));
        return Result.okResult(true);
    }

    @PostMapping("/select")
    public Result<Integer> addSelectItem(
            @RequestBody ItemSelectCreate selectCreate
    ) {
        // TODO: 2021/3/25 check question below the department; check item below question
        var select = server.addItemSelect(selectCreate);

        logger.SuccessOperate("New Select In Item In Question",
                Info.of(selectCreate.questionID, "QuestionID"),
                Info.of(selectCreate.itemID, "ItemID"));
        return Result.okResult(select.sortIndex);
    }

    @PutMapping("/select")
    public Result<Integer> updateSelectItem(
            @RequestBody ItemSelectUpdate selectUpdate
    ) {
        var select = server.updateSelect(selectUpdate);

        logger.SuccessOperate("Update Select In Item In Question",
                Info.of(selectUpdate.questionID, "QuestionID"),
                Info.of(selectUpdate.itemID, "ItemID"),
                Info.of(selectUpdate.selectID, "SelectID"));
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

        logger.SuccessOperate("Remove Select In Item In Question",
                Info.of(id, "QuestionID"),
                Info.of(itemId, "ItemID"),
                Info.of(SelectID, "SelectID"));
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
            var newITemSort = server.addQuestionItem(questionCreate, newItem, item.require, item.unique);

            for (QuestionItemSelect select :
                    item.selects) {
                server.addItemSelect(newITemSort, select.information, select.userInsert);
            }
        }
        server.publicQuestion(questionCreate.ID);

        logger.SuccessOperate("new Full Question",
                Info.of(question.name, "QuestionName"));
        return Result.okResult(true);
    }

    @PostMapping("/public")
    public Result<Boolean> publicQuestion(
            @RequestParam("id") Integer id
    ) {
        logger.dataAccept(Info.of(id, "QuestionID"));
        // TODO: 2021/3/28 visitable problem
        server.publicQuestion(id);

        logger.SuccessOperate("Publish Question",
                Info.of(id, "QuestionID"));
        return Result.okResult(true);
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleNotSelectTypeItem(BaseException exception, HttpServletRequest request) {
        logger.error(request, exception);
        return exception.toResult();
    }
}
