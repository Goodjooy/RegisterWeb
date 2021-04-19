package com.jacky.register.contraller.departmentAdmin.examCycle;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.respond.examCycle.operate.RegisterRespond;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //examCycle 报名
    @GetMapping("/register/{id:\\d+}")
    public Result<RegisterRespond> getRegisterInformation(
            @PathVariable("id") Long id
    ) {
        var examCycle = registerDatabaseService
                .findExamCycleByIdAndDepartment(id, GroupDepartment.lambadaDepartment());

        var question = registerRespondTransformService.getRegister(examCycle.registerQuestionID);

        // TODO: 2021/4/18 logger cover
        return Result.okResult(question);
    }

    @PostMapping("/register/{id:\\d+}")
    public Result<String> studentRegister(
            @PathVariable("id") Long id,
            @RequestBody QuestionCollectionData collection
    ) {
        var student = registerCollectionService.registerStudent(collection, id);
        // TODO: 2021/4/18 logger cover
        return Result.okResult(student.stuID);
    }

    //确认加入考核
    @PostMapping("/exam/confirm/{token:.+}")
    public Result<?> confirmIntoExam(
            @PathVariable("token") String token

    ) {
        var tokenData = registerCollectionService.loadFromToken(token);
        registerCollectionService.confirmIntoExam(tokenData.examId, tokenData.studentId);

        return Result.okResult(tokenData.studentId);
    }

    //考核文件上传
    // TODO: 2021/4/18 考核文件上传
    @PostMapping("/exam/works")
    public Result<?> uploadWorks(
            @RequestParam("examId") Long examId,
            @RequestParam("stuId") String id,
            @RequestParam("stuName") String name,
            @RequestParam("stuEmail") String email,
            @RequestParam("file") MultipartFile file
    ) {
        var works = registerCollectionService.ExamWorkUpload(file, examId, id, name, email);

        // TODO: 2021/4/19 logger cover
        return Result.okResult(works.id);
    }
}
