package com.jacky.register.contraller.departmentAdmin.examCycle;

import com.jacky.register.dataHandle.Info;
import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.respond.examCycle.operate.RegisterRespond;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/examCycle/collection")
public class ExamCycleRegisterController {
    @Autowired
    RegisterDatabaseService registerDatabaseService;
    @Autowired
    RegisterCollectionService registerCollectionService;
    @Autowired
    RegisterRespondTransformService registerRespondTransformService;

    LoggerHandle logger = LoggerHandle.newLogger(ExamCycleRegisterController.class);

    //examCycle 报名
    @GetMapping("/register/{id:\\d+}")
    public Result<RegisterRespond> getRegisterInformation(
            @PathVariable("id") Long id
    ) {
        var examCycle = registerDatabaseService
                .findExamCycleByIdAndDepartment(id, GroupDepartment.lambadaDepartment());

        var question = registerRespondTransformService.getRegister(examCycle.registerQuestionID);

        logger.SuccessOperate(
                "Get Register Information",
                Info.of(id, "ExamCycleID")
        );
        return Result.okResult(question);
    }

    @PostMapping("/register/{id:\\d+}")
    public Result<String> studentRegister(
            @PathVariable("id") Long id,
            @RequestBody QuestionCollectionData collection
    ) {
        var student = registerCollectionService.registerStudent(collection, id);
        // TODO: 2021/4/18 logger cover

        logger.SuccessOperate("Student Register",
                Info.of(id, "ExamCycleID")
        );
        return Result.okResult(student.stuID);
    }

    @GetMapping("/exam/confirm/{stuId:\\d+}/{examId:\\d+}")
    @ResponseStatus(HttpStatus.GONE)
    public Result<String> getToken(
            @PathVariable("stuId") Integer stuId,
            @PathVariable("examId") Long examId
    ) {
        var claim = new RegisterCollectionService.ConfirmToken();
        claim.studentId = stuId;
        claim.examId = examId;
        return Result.okResult(registerCollectionService.generateConfirmToken(claim));
    }

    //确认加入考核
    @PostMapping("/exam/confirm/{token:.+}")
    public Result<?> confirmIntoExam(
            @PathVariable("token") String token

    ) {
        var tokenData = registerCollectionService.loadFromToken(token);
        registerCollectionService.confirmIntoExam(tokenData.examId, tokenData.studentId);

        logger.SuccessOperate("Student Confirm Enter Exam",
                Info.of(tokenData.studentId, "StudentID"),
                Info.of(tokenData.examId, "ExamID"));

        return Result.okResult(tokenData.studentId);
    }

    @PostMapping("/exam/works")
    public Result<?> uploadWorks(
            @RequestParam("examId") Long examId,
            @RequestParam("stuId") String id,
            @RequestParam("stuName") String name,
            @RequestParam("stuEmail") String email,
            @RequestParam("file") MultipartFile file
    ) {
        var works = registerCollectionService.ExamWorkUpload(file, examId, id, name, email);

        logger.SuccessOperate("Student Confirm Enter Exam",
                Info.of(examId, "ExamID"),
                Info.of(id, "StudentID"),
                Info.of(name, "StudentName"),
                Info.of(email, "StudentEmail")
        );
        return Result.okResult(works.id);
    }
}
