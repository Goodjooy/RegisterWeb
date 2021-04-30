package com.jacky.register.contraller.departmentAdmin.examCycle;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.err.register.notFound.StudentNotFoundException;
import com.jacky.register.server.EmailSenderService;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register/info-sender")
public class ExamEmailController {

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    RegisterDatabaseService databaseService;
    @Autowired
    RegisterCollectionService collectionService;
    @Autowired
    DepartmentServer departmentServer;

    @PostMapping("/confirm")
    public Result<?>sendConfirmMessage(
            @RequestParam("examId")Long id,
            @RequestParam("studentId")Integer studentId
    ){
        var department=departmentServer.getFirstDepartment();
        var exam=databaseService.getExam(id,department);
        var examCycle=databaseService.findExamCycleByIdAndDepartment(exam.examCycleID,department);
        var student=exam.termStudents
                .stream().filter(student1 -> student1.id.equals(studentId))
                .findFirst();

        if(student.isEmpty())
            throw new StudentNotFoundException(studentId);

        emailSenderService.sendExamEnterConfirmMail(student.get(),exam,examCycle,department);

        return Result.okResult(true);
    }

    @PostMapping("/message")
    public Result<?>sendAdminMessage(
            @RequestParam("studentId")Integer studentId,
            @RequestParam(value = "title",defaultValue = "untitled")String title,
            @RequestParam(value = "message",defaultValue = "empty")String message
    ){
        var student=databaseService.findStudentById(studentId);
        emailSenderService.sendMessage(student,message,title);

        return Result.okResult(true);
    }


}
