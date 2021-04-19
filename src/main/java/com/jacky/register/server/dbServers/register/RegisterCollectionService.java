package com.jacky.register.server.dbServers.register;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jacky.register.err.register.requireNotSatisfy.TokenTransformFailureException;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItem;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.ExamFinalCollection;
import com.jacky.register.models.database.register.repository.ExamFinalCollectionRepository;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.localFiles.ExamWorksFileStorageService;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

//学生报名收集
@Service
public class RegisterCollectionService {
// TODO: 2021/4/10 学生报名收集表处理

    @Autowired
    RegisterDatabaseService registerDatabaseService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    RegisterRespondTransformService registerRespondTransformService;

    @Autowired
    ExamFinalCollectionRepository finalCollectionRepository;

    @Autowired
    ExamWorksFileStorageService examWorksFileStorageService;

    public Student registerStudent(QuestionCollectionData collection, Long examCycleId) {
        var collect = collection.toQuestionCollection(questionRepository);
        var examCycle = registerDatabaseService
                .findExamCycleByIdAndDepartment(examCycleId, GroupDepartment.lambadaDepartment());
        var linker = registerDatabaseService.findRegisterQuestionById(examCycle.registerQuestionID);

        //map
        HashMap<Integer, CollectionItem> dataHashMap = new HashMap<>();
        for (CollectionItem data :
                collect.items) {
            dataHashMap.put(data.item.id, data);
        }

        //studentData
        Student student = new Student();
        if (dataHashMap.containsKey(linker.studentNameItemID))
            student.name = dataHashMap.get(linker.studentNameItemID).data;
        if (dataHashMap.containsKey(linker.studentItemID))
            student.stuID = dataHashMap.get(linker.studentItemID).data;
        if (dataHashMap.containsKey(linker.emailItemID))
            student.email = dataHashMap.get(linker.emailItemID).data;
        if (dataHashMap.containsKey(linker.qqItemID))
            student.qqID = dataHashMap.get(linker.qqItemID).data;
        if (dataHashMap.containsKey(linker.phoneItemID))
            student.phone = dataHashMap.get(linker.phoneItemID).data;

        return registerDatabaseService.newStudent(student, examCycle.id);
    }

    public void confirmIntoExam(long examId, Integer studentId) {
        registerDatabaseService.studentConfirm(studentId, examId);
    }

    public ConfirmToken loadFromToken(String token) {
        var tokens = JWT.decode(token).getAudience();
        ConfirmToken confirmToken = new ConfirmToken();
        var tokenClazz = confirmToken.getClass();
        var feilds = tokenClazz.getFields();
        try {
            for (int i = 0; i < feilds.length; i++) {
                feilds[i].set(confirmToken, tokens.get(i));
            }
        } catch (IllegalAccessException e) {
            throw new TokenTransformFailureException(e);
        }

        return confirmToken;
    }

    public String generateConfirmToken(ConfirmToken token) {
        var nowTime = LocalDateTime.now().plusHours(2);
        var time = nowTime.atZone(ZoneId.systemDefault()).toInstant();


        var tokenData = JWT.create();
        tokenData.withExpiresAt(Date.from(time));

        tokenData.withAudience(token.studentId.toString(), token.examId.toString());

        return tokenData.sign(Algorithm.HMAC256("1141451919"));

    }


    public static class ConfirmToken {
        public Integer studentId;
        public Long examId;
    }

    public ExamFinalCollection ExamWorkUpload(MultipartFile file, Long examId, String stuId, String stuName, String stuEmail) {
        var student = registerDatabaseService.findStudentByStuIdAndStuName(stuId, stuName, stuEmail);
        var exam = registerDatabaseService.getExam(examId, GroupDepartment.lambadaDepartment());

        //检查学生是否能够参与本轮考核（有报名+通过前面全部考核）
        registerDatabaseService.checkStudentSupport(
                exam,
                registerDatabaseService.findExamCycleByIdAndDepartment(
                        exam.examCycleID,
                        GroupDepartment.lambadaDepartment()),
                student.id);


        //文件上传
        var work = examWorksFileStorageService.storage(file);

        //check exist
        var result=finalCollectionRepository.findByStudentIDAndExamID(student.id, exam.id);
        if (result.isPresent()){
            var temp=result.get();
            //remove old file
            examWorksFileStorageService.delete(temp);

            temp.examFile= work.examFile;
            work=temp;
        }else {
            work.examID=exam.id;
            work.studentID=student.id;
        }

        //save
        finalCollectionRepository.save(work);

        return work;
    }
}
