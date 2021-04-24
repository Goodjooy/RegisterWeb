package com.jacky.register.server.dbServers.register;

import com.jacky.register.models.ExamStatus;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.StudentExamCycleLink;
import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import com.jacky.register.models.database.register.repository.StudentExamLinkRepository;
import com.jacky.register.models.respond.examCycle.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterDataService {
    @Autowired
    RegisterDatabaseService registerDatabaseService;
    @Autowired
    StudentExamLinkRepository examLinkRepository;

    //information get
    public List<ExamCycleInfo> getAllExamCycle(GroupDepartment department) {
        var examCycle = registerDatabaseService
                .examCycleRepository.findByDepartmentID(department.ID);

        return examCycle
                .stream().map(examCycle1 -> {
                    ExamCycleInfo info = new ExamCycleInfo();

                    info.examCycleId = examCycle1.id;
                    info.examCycleName = examCycle1.name;
                    info.departmentId = department.ID;

                    return info;
                })
                .collect(Collectors.toList());
    }

    public List<ExamInfo> getAllExam(Long examCycleID) {
        var exams = registerDatabaseService.findExamByExamCycleId(examCycleID);

        return exams.stream().map(
                exam -> {
                    ExamInfo info = new ExamInfo();

                    info.examId = exam.id;
                    info.examName = exam.name;

                    info.start = LocalDate.now().isBefore(exam.startAt);
                    info.close = LocalDate.now().isAfter(exam.endAt);

                    return info;
                }
        ).collect(Collectors.toList());
    }

    public List<ExamCycleStudentInfo> getAllExamCycleStudentInfo(Long examCycleId) {
        var students = registerDatabaseService.findStudentInExamCycle(examCycleId);

        return students
                .stream().map(student -> {
                    ExamCycleStudentInfo info = new ExamCycleStudentInfo();

                    info.studentId = student.stuID;
                    info.email = student.email;
                    info.name = student.name;

                    info.examStatus = registerDatabaseService
                            .findExamLinkByStudentId(student.id)
                            .stream().map(
                                    examLink -> {
                                        ExamStudentStatus status = new ExamStudentStatus();

                                        status.status = examLink.status;
                                        status.examId = examLink.examId;

                                        return status;
                                    }
                            ).sorted(Comparator.comparing(examStudentStatus -> examStudentStatus.examId))
                            .collect(Collectors.toList());

                    return info;
                }).collect(Collectors.toList());
    }

    public List<ExamStudentInfo> getAllExamStudentInfo(Long examId, GroupDepartment department) {
        var students = registerDatabaseService.findStudentInExam(examId);

        return students.stream()
                .map(student -> {
                    ExamStudentInfo info = new ExamStudentInfo();

                    info.studentId = student.stuID;
                    info.name = student.name;
                    info.email = student.email;

                    info.status = registerDatabaseService.getStudentExamStatues(examId, student.id);

                    info.examWorks = registerDatabaseService.getStudentExamWorks(examId, student.id);

                    return info;
                }).collect(Collectors.toList());
    }

    //考核审核
    //通过 和 不通过
    public void setStudentPassExam(Integer studentId,Long examId){
        var student=registerDatabaseService.getExamLinkByStudentIdAndExamId(studentId, examId);

        student.status= ExamStatus.PASS;

        examLinkRepository.save(student);
    }

    public void setStudentFailureExam(Integer studentId,Long examId){
        var student=registerDatabaseService.getExamLinkByStudentIdAndExamId(studentId, examId);

        student.status= ExamStatus.FAILURE;

        examLinkRepository.save(student);
    }

    //中途插入新学生
    public void insertStudentIntoExam(int studentId,Long targetExamId,GroupDepartment department){
        var student=registerDatabaseService.findStudentById(studentId);
        var exam=registerDatabaseService.findExamById(targetExamId);
        var examCycle=registerDatabaseService.findExamCycleByIdAndDepartment(exam.examCycleID,department);

        var registerStudentIds=examCycle.studentSet.stream()
                .map(student1 -> student1.id).collect(Collectors.toSet());

        //如果学生没有报名考核周期
        if (!registerStudentIds.contains(studentId)){
            StudentExamCycleLink examCycleLink=new StudentExamCycleLink();
            examCycleLink.examCycleId=examCycle.id;
            examCycleLink.studentID=student.id;

            registerDatabaseService.newStudent(student,examCycle.id);
        }

        //如果学生不在本轮考核

        var examStudentIds=exam.termStudents.stream()
                .map(student1 -> student1.id)
                .collect(Collectors.toSet());

        if (!examStudentIds.contains(student.id)){
            //新建之前全部的测试信息
            var preExams=examCycle.examList
                    .stream()
                    .filter(exam1 -> exam1.id<exam.id)
                    .collect(Collectors.toList());

            var ExamLinkers=preExams.stream()
                    .filter(exam1 -> !exam1.termStudents.contains(student))
                    .map(exam1 -> {
                        StudentExamLink examLink=new StudentExamLink();

                        examLink.status=ExamStatus.ADMIN_SET;
                        examLink.examId=exam1.id;
                        examLink.studentID=student.id;

                        return examLink;
                    }).collect(Collectors.toList());

            examLinkRepository.saveAll(ExamLinkers);
        }

        //创建本轮考核
        //检查存在
        if(!exam.termStudents.contains(student)){
            //不存在
            StudentExamLink examLink=new StudentExamLink();

            examLink.examId=exam.id;
            examLink.status=ExamStatus.REGISTER;
            examLink.studentID=student.id;

            examLinkRepository.save(examLink);
        }
    }

}
