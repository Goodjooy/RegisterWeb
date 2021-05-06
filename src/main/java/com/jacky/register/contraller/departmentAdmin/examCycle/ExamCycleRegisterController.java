package com.jacky.register.contraller.departmentAdmin.examCycle;

import com.jacky.register.dataHandle.Info;
import com.jacky.register.dataHandle.LoggerHandle;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.err.BaseException;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.respond.examCycle.operate.RegisterRespond;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import com.jacky.register.server.localFiles.ExamRequireFileStorageService;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/examCycle/collection")
public class ExamCycleRegisterController {
    @Autowired
    RegisterDatabaseService registerDatabaseService;
    @Autowired
    RegisterCollectionService registerCollectionService;
    @Autowired
    RegisterRespondTransformService registerRespondTransformService;
    @Autowired
    ExamRequireFileStorageService examRequireFileStorageService;

    LoggerHandle logger = LoggerHandle.newLogger(ExamCycleRegisterController.class);

    //examCycle 报名
    @GetMapping("/register/{id:\\d+}")
    @ResponseBody
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
    @ResponseBody
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

    @GetMapping("/exam/requireFile")
    @ResponseBody
    public ResponseEntity<Resource> getRequireFile(
            @RequestParam("examId") Long examId
    ) {
        var exam = registerDatabaseService.getExam(examId, GroupDepartment.lambadaDepartment());
        return ResponseEntity
                .ok()
                .body(examRequireFileStorageService
                        .loadAsResource(exam));
    }

    @GetMapping("/exam/confirm/generate/{stuId:\\d+}/{examId:\\d+}")
    @ResponseStatus(HttpStatus.GONE)
    @ResponseBody
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

    @RequestMapping(value = "/exam/confirm/{token:.+}",method = {RequestMethod.GET,RequestMethod.POST})
    public String confirmIntoExam(
            @PathVariable("token") String token,
            Model model
    ) {
        var tokenData = registerCollectionService.loadFromToken(token);
        registerCollectionService.confirmIntoExam(tokenData.examId, tokenData.studentId,model);

        logger.SuccessOperate("Student Confirm Enter Exam",
                Info.of(tokenData.studentId, "StudentID"),
                Info.of(tokenData.examId, "ExamID"));

        return "comfirmOK";
    }

    @PostMapping("/exam/works")
    @ResponseBody
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
    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result<?> handleNotSelectTypeItem(BaseException exception, HttpServletRequest request) {
        logger.error(request, exception);
        return exception.toResult();
    }
}
