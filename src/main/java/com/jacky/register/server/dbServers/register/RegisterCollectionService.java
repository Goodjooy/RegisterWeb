package com.jacky.register.server.dbServers.register;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jacky.register.err.register.requireNotSatisfy.TokenTransformFailureException;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItem;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            student.stdID = dataHashMap.get(linker.studentItemID).data;
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

}
