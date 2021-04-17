package com.jacky.register.server.dbServers.register;

import com.jacky.register.err.register.notFound.ExamCycleNotFoundException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.Term.repository.ExamCycleRepository;
import com.jacky.register.models.database.Term.repository.ExamRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.RegisterQuestion;
import com.jacky.register.models.database.register.repository.RegisterQuestionRepository;
import com.jacky.register.models.request.register.examCycle.ExamCycleData;
import com.jacky.register.models.request.register.examCycle.ExamData;
import com.jacky.register.models.request.register.examCycle.QuestionLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

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

    //新建考核周期
    public ExamCycle newTermCycle(GroupDepartment department, ExamCycleData examCycle) {
        ExamCycle cycle = new ExamCycle();
        var register = getRegisterRelation(examCycle.linker);

        registerQuestionRepository.save(register);

        //部门绑定
        cycle.departmentID = department.ID;

        cycle.name = examCycle.name;
        cycle.registerQuestionID = register.id;

        examCycleRepository.save(cycle);

        return cycle;
    }

    public ExamCycle updateExamCycle(GroupDepartment department, ExamCycleData examCycle, Long id) {
        var cycle = getExamCycle(id, department);
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

    public void removeExamCycle(GroupDepartment department, Long id) {
        var result = examCycleRepository.findByIdAndDepartmentID(id, department.ID);
        if (result.isEmpty())
            throw new ExamCycleNotFoundException(id, department);

        // TODO: 2021/4/17 完全删除考核周期

    }

    public Exam addExamIntoExamCycle(GroupDepartment department, ExamData exam) {
        var examCycle = getExamCycle(exam.cycleID, department);

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
        var exam = getExam(examId, department);

        exam.name = examData.name;
        exam.examCycleID = examData.cycleID;
        exam.endAt = examData.endAt;
        exam.startAt = examData.startAt;

        examRepository.save(exam);
        return exam;
    }


    public Exam fileUpload(MultipartFile file, Exam exam, GroupDepartment department) {
        // TODO: 2021/4/10 file Upload Service
        return null;
    }

    // TODO: 2021/4/10 exam cycle update/delete

    RegisterQuestion getRegisterRelation(QuestionLinker linker) {
        RegisterQuestion question = new RegisterQuestion();

        //问卷绑定
        question.questionID = linker.questionID;

        //字段绑定
        question.emailItemID = linker.emailItemID;
        question.qqItemID = linker.qqItemID;
        question.studentItemID = linker.studentIDItemID;
        question.studentNameItemID = linker.studentNameItemID;
        question.phoneItemID= linker.phoneItemID;

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
}
