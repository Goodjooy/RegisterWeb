package com.jacky.register.server.modelTransformServers;

import com.jacky.register.err.register.notFound.RegisterQuestionNotFoundException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import com.jacky.register.models.database.register.repository.RegisterQuestionRepository;
import com.jacky.register.models.respond.examCycle.operate.*;
import com.jacky.register.models.respond.question.control.Question;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.qustion.QuestionControlServer;
import com.jacky.register.server.dbServers.register.RegisterDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RegisterRespondTransformService {
    @Autowired
    RegisterDatabaseService databaseService;

    @Autowired
    QuestionControlServer questionControlServer;

    @Autowired
    DepartmentServer departmentServer;

    @Autowired
    RegisterQuestionRepository registerQuestionRepository;

    public ExamCycleRespond toRespond(ExamCycle examCycle){
        ExamCycleRespond examCycleRespond=new ExamCycleRespond();

        examCycleRespond.id= examCycle.id;
        examCycleRespond.name= examCycle.name;
        examCycleRespond.departmentId=examCycle.departmentID;
        examCycleRespond.departName=departmentServer.getDepartmentByID(examCycle.departmentID).name;

        examCycleRespond.register=getRegister(examCycle.registerQuestionID);
        examCycleRespond.exams=examCycle.examList.stream().map(this::toRespond).collect(Collectors.toList());
        examCycleRespond.students=examCycle.studentSet.stream().map(this::toRespond).collect(Collectors.toList());

        return examCycleRespond;
    }
    public ExamRespond toRespond(Exam exam){
        ExamRespond examRespond=new ExamRespond();

        examRespond.id=exam.id;
        examRespond.name=exam.name;
        examRespond.examFileName= exam.requireFile;

        examRespond.startAt=exam.startAt;
        examRespond.endAt=exam.endAt;

        return examRespond;
    }

    public StudentRespond toRespond(Student student){
        StudentRespond studentRespond=new StudentRespond();

        studentRespond.id=student.id;

        studentRespond.email=student.email;
        studentRespond.studentId=student.stuID;
        studentRespond.name=student.name;
        studentRespond.phone=student.phone;
        studentRespond.qqId=student.qqID;

        studentRespond.status=databaseService.findExamLinkByStudentId(student.id)
                .stream().map(this::toRespond).collect(Collectors.toList());

        return studentRespond;
    }

    public StudentStatusRespond toRespond(StudentExamLink examLink){
        StudentStatusRespond studentStatusRespond=new StudentStatusRespond();

        studentStatusRespond.ExamId= examLink.examId;
        studentStatusRespond.status=examLink.status;
        return studentStatusRespond;
    }



    public RegisterRespond getRegister(Long registerId){
        var result=registerQuestionRepository.findById(registerId);
        if (result.isEmpty())
            throw new RegisterQuestionNotFoundException(registerId);

        var register=result.get();

        var question=questionControlServer.getQuestionByID(register.questionID);

        RegisterRespond registerRespond=new RegisterRespond();
        registerRespond.question= Question.fromQuestion(question);
        return registerRespond;

    }



}
