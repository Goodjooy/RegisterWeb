package com.jacky.register.server.dbServers.register;

import com.jacky.register.err.register.notFound.ExamCycleNotFoundException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.Term.repository.ExamCycleRepository;
import com.jacky.register.models.database.Term.repository.ExamRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.RegisterQuestion;
import com.jacky.register.models.database.register.repository.RegisterQuestionRepository;
import com.jacky.register.models.request.register.examCycle.CreateExam;
import com.jacky.register.models.request.register.examCycle.CreateExamCycle;
import com.jacky.register.models.request.register.examCycle.QuestionLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public ExamCycle newTermCycle(GroupDepartment department, CreateExamCycle examCycle) {
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

    public Exam addExamIntoExamCycle(GroupDepartment department, CreateExam exam){
        var result=examCycleRepository.findByIdAndDepartmentID(exam.cycleID,department.ID);
        if (result.isEmpty())
            throw new ExamCycleNotFoundException(exam.cycleID, department);

        Exam exam1=new Exam();

        exam1.startAt=exam.startAt;
        exam1.endAt=exam.endAt;

        exam1.examCycleID=result.get().id;

        return exam1;
    }
    public Exam fileUpload(MultipartFile file,Exam exam,GroupDepartment department){
        // TODO: 2021/4/10 file Upload Service
        return null;
    }

    // TODO: 2021/4/10 exam cycle update/delete

    RegisterQuestion getRegisterRelation(QuestionLinker linker) {
        RegisterQuestion question = new RegisterQuestion();

        //问卷绑定
        question.questionID = linker.questionId;

        //字段绑定
        question.emailItemID = linker.emailItemID;
        question.qqItemID = linker.qqItemID;
        question.studentItemID = linker.studentItemID;
        question.studentNameItemID = linker.studentNameItemID;

        return question;
    }
}
