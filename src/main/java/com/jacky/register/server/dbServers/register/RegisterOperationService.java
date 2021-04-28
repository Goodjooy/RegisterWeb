package com.jacky.register.server.dbServers.register;

import com.jacky.register.err.register.notFound.ExamCycleNotFoundException;
import com.jacky.register.err.register.notFound.RegisterQuestionNotFoundException;
import com.jacky.register.err.register.requireNotSatisfy.ExamCycleClosedException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.Term.repository.ExamCycleRepository;
import com.jacky.register.models.database.Term.repository.ExamRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.RegisterQuestion;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.repository.RegisterQuestionRepository;
import com.jacky.register.models.request.register.examCycle.ExamCycleData;
import com.jacky.register.models.request.register.examCycle.ExamData;
import com.jacky.register.models.request.register.examCycle.QuestionLinker;
import com.jacky.register.models.status.ExamStatus;
import com.jacky.register.server.localFiles.ExamRequireFileStorageService;
import com.jacky.register.server.localFiles.ExamWorksFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 管理员注册信息操作
 */
@Service
public class RegisterOperationService {
    @Autowired
    RegisterQuestionRepository registerQuestionRepository;
    @Autowired
    ExamCycleRepository examCycleRepository;
    @Autowired
    ExamRepository examRepository;

    @Autowired
    RegisterDatabaseService databaseService;
    @Autowired
    ExamRequireFileStorageService examRequireFileStorageService;
    @Autowired
    ExamWorksFileStorageService examWorksFileStorageService;

    //新建考核周期
    public ExamCycle newTermCycle(GroupDepartment department, ExamCycleData examCycle) {
        ExamCycle cycle = new ExamCycle();
        var register = getRegisterRelation(examCycle.linker);

        registerQuestionRepository.save(register);

        //部门绑定
        cycle.departmentID = department.ID;

        cycle.name = examCycle.name;
        cycle.registerQuestionID = register.id;

        cycle.doneExamCycle = false;

        examCycleRepository.save(cycle);

        return cycle;
    }

    public ExamCycle updateExamCycle(GroupDepartment department, ExamCycleData examCycle, Long id) {
        var cycle = getExamCycle(id, department);
        checkExamCycle(cycle);
        //remove old linker
        var newLinker = getRegisterRelation(examCycle.linker);
        var oldLinkerResult = registerQuestionRepository.findById(cycle.registerQuestionID).orElse(null);
        //update Link
        if (sameLinker(oldLinkerResult, newLinker)) {
            registerQuestionRepository.deleteById(cycle.registerQuestionID);
            registerQuestionRepository.save(newLinker);
        }
        //部门绑定
        cycle.departmentID = department.ID;
        cycle.name = examCycle.name;
        cycle.registerQuestionID = newLinker.id;
        examCycleRepository.save(cycle);
        return cycle;
    }

    public ExamCycle closeExamCycle(Long id, GroupDepartment department) {
        var cycle = getExamCycle(id, department);
        checkExamCycle(cycle);

        cycle.doneExamCycle = true;

        examCycleRepository.save(cycle);

        return cycle;
    }


    public void removeExamCycle(GroupDepartment department, Long id) {
        var result = examCycleRepository.findByIdAndDepartmentID(id, department.ID);
        if (result.isEmpty())
            throw new ExamCycleNotFoundException(id, department);
        var examCycle = result.get();
        // TODO: 2021/4/17 完全删除考核周期
        //删除考核周期的考核轮
        var exams=databaseService.findExamByExamCycleId(examCycle.id)
                .stream()
                .map(this::removeExam)
                .collect(Collectors.toList());

        //删除本考核轮的学生
        var students=databaseService.findStudentInExamCycle(examCycle.id)
                .stream()
                .map(student -> removeExamCycleStudent(student,examCycle))
                .collect(Collectors.toList());

        //删除考核周期
        examCycleRepository.deleteByIdAndDepartmentID(id,department.ID);
    }

    public boolean changeExamCycleRegisterStatus(GroupDepartment department, long id) {
        var cycle = getExamCycle(id, department);
        var result = registerQuestionRepository.findById(cycle.registerQuestionID);
        if (result.isEmpty())
            throw new RegisterQuestionNotFoundException(cycle.registerQuestionID);

        var question = result.get();
        question.available = !question.available;

        question = registerQuestionRepository.save(question);

        return question.available;
    }

    public Exam addExamIntoExamCycle(GroupDepartment department, ExamData exam) {
        var examCycle = getExamCycle(exam.cycleID, department);
        checkExamCycle(examCycle);
        Exam exam1 = new Exam();

        exam1.name = exam.name;
        exam1.startAt = exam.startAt;
        exam1.endAt = exam.endAt;

        exam1.examCycleID = examCycle.id;

        examRepository.save(exam1);
        return exam1;
    }

    public Exam updateExam(GroupDepartment department, ExamData examData, Long examId) {
        var examCycle = getExamCycle(examData.cycleID, department);
        checkExamCycle(examCycle);
        var exam = getExam(examId, department);

        exam.name = examData.name;
        exam.examCycleID = examData.cycleID;
        exam.endAt = examData.endAt;
        exam.startAt = examData.startAt;

        examRepository.save(exam);
        return exam;
    }

    public Exam fileUpload(MultipartFile file, Exam exam) {
        var examDate = examRequireFileStorageService.storage(file);

        exam.requireFile = examDate.requireFile;

        //save exam;
        examRepository.save(exam);
        return exam;
    }

    public Exam removeExam(Exam exam) {
        //删除所有和本轮考核有关的学生
        var students = databaseService.findStudentInExam(exam.id)
                .stream()
                .map(student -> removeExamStudent(student, exam))
                .collect(Collectors.toSet());

        //移除考核周期
        //移除文件
        if (exam.requireFile != null) {
            examRequireFileStorageService.delete(exam);
        }
        examRepository.delete(exam);
        return exam;
    }

    public Student removeExamStudent(Student student, Exam exam) {
        var linker = databaseService.getExamLinkByStudentIdAndExamId(student.id, exam.id);

        //删除提交的作品
        if (linker.status != ExamStatus.REGISTER && linker.status != ExamStatus.ADMIN_SET) {
            var file = databaseService.getStudentExamWorks(exam.id, student.id);
            examWorksFileStorageService.delete(file);
        }
        //删除连接
        databaseService.examLinkRepository.deleteByStudentIDAndExamId(student.id, exam.id);
        //移除考核周期的学生不会移除学生本身

        return student;
    }
    public Student removeExamCycleStudent(Student student,ExamCycle examCycle){
        //删除全部考核周期学生
        var exams=databaseService.findExamByExamCycleId(examCycle.id)
                .stream()
                .map(exam -> removeExamStudent(student,exam))
                .collect(Collectors.toList());

        databaseService.examCycleLinkRepository.deleteByExamCycleIdAndStudentID(examCycle.id, student.id);

        //检查学生是否没有参与任何考核周期了
        if(databaseService.examCycleLinkRepository.countByStudentID(student.id)==0)
            databaseService.studentRepository.delete(student);

        return student;
    }


    RegisterQuestion getRegisterRelation(QuestionLinker linker) {
        RegisterQuestion question = new RegisterQuestion();

        //问卷绑定
        question.questionID = linker.questionID;

        //字段绑定
        question.emailItemID = linker.emailItemID;
        question.qqItemID = linker.qqItemID;
        question.studentItemID = linker.studentIDItemID;
        question.studentNameItemID = linker.studentNameItemID;
        question.phoneItemID = linker.phoneItemID;

        question.endAt = linker.endAt;
        question.available = true;

        return question;
    }

    boolean sameLinker(RegisterQuestion old, RegisterQuestion n) {
        if (old == null)
            return false;
        return n == null || Objects.equals(old.questionID, n.questionID) &&
                Objects.equals(old.studentNameItemID, n.studentNameItemID) &&
                Objects.equals(old.studentItemID, n.studentItemID) &&
                Objects.equals(old.qqItemID, n.qqItemID) &&
                Objects.equals(old.emailItemID, n.emailItemID);
    }

    ExamCycle getExamCycle(Long id, GroupDepartment department) {
        Optional<ExamCycle> result;
        if (department.ID == -1) {
            result = examCycleRepository.findById(id);
        } else {
            result = examCycleRepository.findByIdAndDepartmentID(id, department.ID);
        }

        if (result.isEmpty())
            throw new ExamCycleNotFoundException(id, department);
        return result.get();
    }

    Exam getExam(Long id, GroupDepartment department) {
        Optional<Exam> result;
        result = examRepository.findById(id);


        if (result.isEmpty())
            throw new ExamCycleNotFoundException(id, department);
        return result.get();
    }

    static void checkExamCycle(ExamCycle cycle) {
        if (cycle.doneExamCycle)
            throw new ExamCycleClosedException(cycle.id);
    }
}
