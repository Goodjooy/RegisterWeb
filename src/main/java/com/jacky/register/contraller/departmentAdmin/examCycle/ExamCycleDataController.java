package com.jacky.register.contraller.departmentAdmin.examCycle;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.respond.examCycle.data.ExamCycleInfo;
import com.jacky.register.models.respond.examCycle.data.ExamCycleStudentInfo;
import com.jacky.register.models.respond.examCycle.data.ExamInfo;
import com.jacky.register.models.respond.examCycle.data.ExamStudentInfo;
import com.jacky.register.models.status.ExamCycleStatus;
import com.jacky.register.models.status.ExamStatus;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.register.RegisterDataService;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import com.jacky.register.server.dbServers.register.RegisterOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//负责后台的数据处理
//包括为提交同学判断审核通过与否，发送确认邮件，控制学生库（中途插入新学生）

@RestController
@RequestMapping("/api/examCycle/data")
public class ExamCycleDataController {

    @Autowired
    RegisterDataService registerDataService;
    @Autowired
    RegisterDatabaseService databaseService;
    @Autowired
    DepartmentServer departmentServer;
    @Autowired
    RegisterOperationService operationService;

    @GetMapping("/examCycle/all")
    public Result<List<ExamCycleInfo>> getAllBasicExamCycleInfo() {
        var department = departmentServer.getFirstDepartment();
        var examCycles = registerDataService.getAllExamCycle(department);

        return Result.okResult(examCycles);
    }

    @GetMapping("/exam/all")
    public Result<List<ExamInfo>> getAllBasicExamInfo(
            @RequestParam("id") Long examCycleId
    ) {
        var exams = registerDataService.getAllExam(examCycleId);

        return Result.okResult(exams);
    }

    @GetMapping("/examCycle/student/all")
    public Result<List<ExamCycleStudentInfo>> getAllStudentInExamCycle(
            @RequestParam("id") Long examCycleId
    ) {
        var students = registerDataService.getAllExamCycleStudentInfo(examCycleId);

        return Result.okResult(students);
    }

    //过滤器\翻页支持
    @GetMapping("/examCycle/student/{key:.+}")
    public Result<List<ExamCycleStudentInfo>> getFilteredStudentInfoInExamCycle(
            @RequestParam("id") Long examCycleId,
            @RequestParam(value = "page", defaultValue = "-1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "-1") Integer pageSize,
            @RequestParam(value = "studentMode", defaultValue = "ALL") ExamCycleStatus status,
            @PathVariable(value = "key") String key
    ) {
        var students = registerDataService
                .getSearchedExamCycleStudentInfo(examCycleId,key);

        if (status != ExamCycleStatus.ALL) {
            students = students.stream()
                    .filter(examCycleStudentInfo -> examCycleStudentInfo.examCycleStatus.status==status)
                    .collect(Collectors.toList());
        }

        if (page != -1 && pageSize != -1) {
            var startIndex = pageSize * page;
            var endIndex = startIndex + pageSize;

            students=students.subList(startIndex,endIndex);
        }

        return Result.okResult(students);
    }

    @DeleteMapping("/examCycle/student")
    public Result<Integer>removeStudentInExamCycle(
            @RequestParam("id")Long examCycleId,
            @RequestParam("studentId")Integer studentId
    ){
        var stu=operationService.removeExamCycleStudent(studentId,examCycleId);
        return Result.okResult(stu);
    }


    @GetMapping("/exam/student/all")
    public Result<List<ExamStudentInfo>> getAllStudentInExam(
            @RequestParam("examId") Long examId
    ) {
        var department = departmentServer.getFirstDepartment();
        var students = registerDataService.getAllExamStudentInfo(examId);

        return Result.okResult(students);
    }

    @GetMapping("/exam/student/filter/{key:.+}")
    public Result<List<ExamStudentInfo>>searchStudentInExamCycle(
            @RequestParam("id") Long examId,
            @RequestParam(value = "page", defaultValue = "-1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "-1") Integer pageSize,
            @RequestParam(value = "studentMode", defaultValue = "ALL") ExamStatus status,
            @PathVariable(value = "key") String key
    ){
        var students = registerDataService
                .getSearchExamStudentInfo(examId,key);

        if (status != ExamStatus.ALL) {
            students = students.stream()
                    .filter(examStudentInfo -> examStudentInfo.status==status)
                    .collect(Collectors.toList());
        }

        if (page != -1 && pageSize != -1) {
            var startIndex = pageSize * page;
            var endIndex = startIndex + pageSize;

            students=students.subList(startIndex,endIndex);
        }
        return Result.okResult(students);
    }
    @DeleteMapping("/exam/student")
    public Result<?>removeStudentInExam(
            @RequestParam("id")Long examId,
            @RequestParam("studentId")Integer studentId
    ){
        var stu=operationService.removeExamStudentWithBefore(studentId,examId);
        return Result.okResult(stu);
    }

    @GetMapping("/exam/student/works")
    public ResponseEntity<Resource> getStudentWork(
            @RequestParam("id") Integer stuId,
            @RequestParam("examId") Long examId
    ) {
        var resource = registerDataService.getStudentWork(stuId, examId);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @PostMapping("/exam/student/status")
    public Result<ExamStatus> setStudentExamStatus(
            @RequestParam("id") Integer stuId,
            @RequestParam("examId") Long examId,
            @RequestParam(value = "isPass", defaultValue = "ture") Boolean isPass
    ) {

        ExamStatus status;
        if (isPass) {
            status = registerDataService.setStudentPassExam(stuId, examId);
        } else
            status = registerDataService.setStudentFailureExam(stuId, examId);

        return Result.okResult(status);
    }

    @PostMapping("/examCycle/student/status")
    public Result<ExamCycleStatus>setStudentExamCycleStatus(
            @RequestParam("id") Integer stuId,
            @RequestParam("examCycleId") Long examCycleId,
            @RequestParam(value = "isPass", defaultValue = "true") Boolean isPass
    ){
        ExamCycleStatus status;
        if(isPass)
            status=ExamCycleStatus.PASS;
        else
            status=ExamCycleStatus.REJECT;

        status=registerDataService.setStudentExamCycleStatus(stuId,examCycleId,status);

        return Result.okResult(status);
    }

    @PostMapping("/exam/student/insert")
    public Result<?> insertNewStudent(
            @RequestParam("id") Integer stuId,
            @RequestParam("examId") Long examId
    ) {
        registerDataService.insertStudentIntoExam(stuId, examId, departmentServer.getFirstDepartment());

        return Result.okResult(true);
    }

}
