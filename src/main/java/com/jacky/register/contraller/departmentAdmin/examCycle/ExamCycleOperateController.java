package com.jacky.register.contraller.departmentAdmin.examCycle;

import com.jacky.register.contraller.departmentAdmin.register.RegisterOperationController;
import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.Term.repository.ExamCycleRepository;
import com.jacky.register.models.request.register.examCycle.CreateExam;
import com.jacky.register.models.request.register.examCycle.CreateExamCycle;
import com.jacky.register.models.request.register.examCycle.UpdateExam;
import com.jacky.register.models.request.register.examCycle.UpdateExamCycle;
import com.jacky.register.models.respond.examCycle.operate.ExamCycleRespond;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import com.jacky.register.server.dbServers.register.RegisterOperationService;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RestController
@RequestMapping("/api/examCycle/operate")
public class ExamCycleOperateController {
    @Autowired
    RegisterOperationService operationService;
    @Autowired
    RegisterDatabaseService databaseService;
@Autowired
    DepartmentServer departmentServer;
@Autowired
    RegisterRespondTransformService registerRespondTransformService;

    @GetMapping("/examCycle/all")
    public Result<List<?>>getAllExamCycle(
            @RequestParam(value = "pageSize",defaultValue = "-1")Integer pageSize,
            @RequestParam(value = "pageNumber",defaultValue ="1" )Integer pageNumber
            ){
        // TODO: 2021/4/17 支持翻页功能的考核周期简略图
        return null;
    }

    @GetMapping("/examCycle")
    public Result<ExamCycleRespond>getExamCycle(
            @RequestParam("id")Long id
    ){
        var department=departmentServer.getFirstDepartment();
        var examCycle=databaseService.findExamCycleByIdAndDepartment(id,department);
        var examCycleRespond=registerRespondTransformService.toRespond(examCycle);
        return Result.okResult(examCycleRespond);
    }


    @PostMapping("/examCycle")
    public Result<?>newExamCycle(
            @RequestBody CreateExamCycle examCycle
            ){
        // TODO: 2021/4/17 部门确定
        var department=departmentServer.getFirstDepartment();

        var cycle=operationService.newTermCycle(department,examCycle.data);

        return Result.okResult(cycle.id);
    }

    @PutMapping("/examCycle")
    public Result<?>updateExamCycle(
            @RequestBody UpdateExamCycle examCycle
            ){
        // TODO: 2021/4/17 部门用户认证进入
        var department=departmentServer.getFirstDepartment();

        var cycle=operationService.updateExamCycle(department, examCycle.data, examCycle.id);
        return Result.okResult(cycle.id);
    }

    @DeleteMapping("/examCycle")
    // TODO: 2021/4/17 考核周期删除
    @ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "模块未完成")
    public Result<?>deleteExamCycle(
            @RequestParam("id")Long id
    ){
        var department=departmentServer.getFirstDepartment();
        return null;
    }

    @PostMapping("/exam")
    public Result<Long>newExamInExamCycle(
            @RequestBody CreateExam exam
            ){
        //todo department check
        var department=departmentServer.getFirstDepartment();

        var newExam=operationService.addExamIntoExamCycle(department,exam.data);

        return Result.okResult(newExam.id);
    }

    @PostMapping("/exam/file")
    @ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "模块未完成")
    public Result<?>uploadExamRequireFile(
            @RequestParam("id")Long examId,
            @RequestParam("file") MultipartFile file
    ){
        // TODO: 2021/4/17 考核要求文件上传
        return null;
    }

    @PutMapping("/exam")
    public Result<?>updateExamInExamCycle(
            @RequestBody UpdateExam exam
            ){
        var department=departmentServer.getFirstDepartment();

        var newExam=operationService.updateExam(department, exam.data, exam.examId);

        return Result.okResult(newExam.id);
    }
}
