package com.jacky.register.server.dbServers.register;

import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import com.jacky.register.models.database.register.repository.StudentExamLinkRepository;
import com.jacky.register.models.respond.examCycle.data.*;
import com.jacky.register.models.status.ExamCycleStatus;
import com.jacky.register.models.status.ExamStatus;
import com.jacky.register.server.localFiles.ExamWorksFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegisterDataService {
    @Autowired
    RegisterDatabaseService registerDatabaseService;
    @Autowired
    StudentExamLinkRepository examLinkRepository;

    @Autowired
    ExamWorksFileStorageService worksFileStorageService;

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

        return toExamCycleStudentInfo(students, examCycleId);
    }

    public List<ExamCycleStudentInfo> getSearchedExamCycleStudentInfo(long examCycleId, String keyWord) {
        if (keyWord == null || keyWord.equals(""))
            return getAllExamCycleStudentInfo(examCycleId);

        var students = registerDatabaseService.findKeyWordStudentInExamCycle(examCycleId, keyWord);
        return toExamCycleStudentInfo(students, examCycleId);
    }

    List<ExamCycleStudentInfo> toExamCycleStudentInfo(Set<Student> students, long examCycleId) {
        return students
                .stream().map(student -> {
                    ExamCycleStudentInfo info = new ExamCycleStudentInfo();

                    info.studentId = student.stuID;
                    info.email = student.email;
                    info.name = student.name;
                    info.qq = student.qqID;
                    info.id = student.id;

                    info.examCycleStatus = new ExamCycleStudentStatus();
                    info.examCycleStatus.examCycleId = examCycleId;
                    info.examCycleStatus.status = registerDatabaseService
                            .getStudentExamCycleStatus(student, examCycleId);

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

    public List<ExamStudentInfo> getAllExamStudentInfo(Long examId) {
        var students = registerDatabaseService.findStudentInExam(examId);

        return toExamStudentInfo(students, examId);
    }

    public List<ExamStudentInfo> getSearchExamStudentInfo(Long examId, String keyWord) {
        if (keyWord == null || keyWord.equals(""))
            return getAllExamStudentInfo(examId);

        var students = registerDatabaseService.searchStudentInExam(examId, keyWord);

        return toExamStudentInfo(students, examId);
    }

    List<ExamStudentInfo> toExamStudentInfo(Set<Student> students, long examId) {
        return students.stream()
                .map(student -> {
                    ExamStudentInfo info = new ExamStudentInfo();

                    info.id = student.id;

                    info.studentId = student.stuID;
                    info.name = student.name;
                    info.email = student.email;
                    info.qq = student.qqID;

                    info.status = registerDatabaseService.getStudentExamStatues(examId, student.id);

                    info.examWorks = registerDatabaseService.getStudentExamWorks(examId, student.id);

                    return info;
                }).collect(Collectors.toList());
    }

    public Resource getStudentWork(Integer stuId, long examId) {
        var result = registerDatabaseService.findStudentById(stuId);
        var stuWorks = registerDatabaseService.getStudentExamWorks(examId, result.id);

        return worksFileStorageService.loadAsResource(stuWorks);
    }

    //考核审核
    //通过 和 不通过
    public ExamStatus setStudentPassExam(Integer studentId, Long examId) {
        var student = registerDatabaseService.getExamLinkByStudentIdAndExamId(studentId, examId);

        student.status = ExamStatus.PASS;

        examLinkRepository.save(student);

        return ExamStatus.PASS;
    }

    public ExamStatus setStudentFailureExam(Integer studentId, Long examId) {
        var student = registerDatabaseService.getExamLinkByStudentIdAndExamId(studentId, examId);

        student.status = ExamStatus.FAILURE;

        examLinkRepository.save(student);

        return ExamStatus.FAILURE;
    }

    public ExamCycleStatus setStudentExamCycleStatus(int studentId,long examCycleId,ExamCycleStatus status){
        var student=registerDatabaseService.findStudentExamCycleLinkByStudentAndExamCycleID(
                studentId,
                examCycleId
        );
        student.status=status;
        registerDatabaseService.examCycleLinkRepository.save(student);
        return status;

    }

    //中途插入新学生
    public void insertStudentIntoExam(int studentId, Long targetExamId, GroupDepartment department) {
        var student = registerDatabaseService.findStudentById(studentId);
        var exam = registerDatabaseService.findExamById(targetExamId);
        var examCycle = registerDatabaseService.findExamCycleByIdAndDepartment(exam.examCycleID, department);

        var registerStudentIds = examCycle.studentSet.stream()
                .map(student1 -> student1.id).collect(Collectors.toSet());

        //如果学生没有报名考核周期
        if (!registerStudentIds.contains(studentId)) {
            registerDatabaseService.newStatusStudent(student, examCycle.id, ExamCycleStatus.ADMIN_SET);
        } else {
            //设置接受
            registerDatabaseService.setExamCycleStatus(student, examCycle.id, ExamCycleStatus.ADMIN_SET);
        }


        //如果学生不在本轮考核

        var examStudentIds = exam.termStudents.stream()
                .map(student1 -> student1.id)
                .collect(Collectors.toSet());

        if (!examStudentIds.contains(student.id)) {
            //新建之前全部的测试信息
            var preExams = examCycle.examList
                    .stream()
                    .filter(exam1 -> exam1.id < exam.id)
                    .collect(Collectors.toList());

            var ExamLinkers = preExams.stream()
                    .filter(exam1 -> !exam1.termStudents.contains(student))
                    .map(exam1 -> {
                        StudentExamLink examLink = new StudentExamLink();

                        examLink.status = ExamStatus.ADMIN_SET;
                        examLink.examId = exam1.id;
                        examLink.studentID = student.id;

                        return examLink;
                    }).collect(Collectors.toList());

            examLinkRepository.saveAll(ExamLinkers);
        }

        //创建本轮考核
        //检查存在
        if (!exam.termStudents.contains(student)) {
            //不存在
            StudentExamLink examLink = new StudentExamLink();

            examLink.examId = exam.id;
            examLink.status = ExamStatus.REGISTER;
            examLink.studentID = student.id;

            examLinkRepository.save(examLink);
        }
    }

    public Integer getStudentId(String stuName, String stuId, String stuEmail) {
        var student = registerDatabaseService.findStudentByStuIdAndStuName(stuName, stuId, stuEmail);
        return student.id;
    }

}
