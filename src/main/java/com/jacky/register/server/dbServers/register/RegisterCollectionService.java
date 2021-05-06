package com.jacky.register.server.dbServers.register;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jacky.register.err.register.notFound.ExamCycleNotFoundException;
import com.jacky.register.err.register.requireNotSatisfy.HandOnWorksOverTimeException;
import com.jacky.register.err.register.requireNotSatisfy.TokenTransformFailureException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.request.register.student.StudentData;
import com.jacky.register.models.request.register.student.StudentUpdate;
import com.jacky.register.models.status.ExamStatus;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.quetionail.QuestionRepository;
import com.jacky.register.models.database.quetionail.collection.CollectionItem;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.ExamFinalCollection;
import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import com.jacky.register.models.database.register.repository.ExamFinalCollectionRepository;
import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import com.jacky.register.server.dbServers.qustion.QuestionCollectionServer;
import com.jacky.register.server.localFiles.ExamWorksFileStorageService;
import com.jacky.register.server.modelTransformServers.RegisterRespondTransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    QuestionCollectionServer questionCollectionServer;
    public Student createStudent(StudentData data){
        Student student=new Student();
        student.qqID=data.qqId;
        student.stuID=data.studentId;
        student.phone=data.phone;
        student.email= data.email;
        student.name=data.name;

        return registerDatabaseService.checkStudentExist(student);

    }
    public Student updateStudentInfo(StudentData data,Integer studentId){
        var student=registerDatabaseService.findStudentById(studentId);

        student.qqID=data.qqId;
        student.stuID=data.studentId;
        student.phone=data.phone;
        student.email= data.email;
        student.name=data.name;

        return registerDatabaseService.studentRepository.save(student);
    }

    public Student registerStudent(QuestionCollectionData collection, Long examCycleId) {
        var examCycle = registerDatabaseService
                .findExamCycleByIdAndDepartment(examCycleId, GroupDepartment.lambadaDepartment());
        RegisterOperationService.checkExamCycle(examCycle);
        //todo exam cycle register closed ,return


        var linker = registerDatabaseService.findRegisterQuestionById(examCycle.registerQuestionID);

        collection.QuestionID=linker.questionID;
        collection.time=LocalDateTime.now();

        var collect = collection.toQuestionCollection(questionRepository);

        //map
        HashMap<Integer, CollectionItem> dataHashMap = new HashMap<>();
        for (CollectionItem data :
                collect.items) {
            dataHashMap.put(data.item.sortIndex, data);
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
        //questionCollectionServer.sendQuestionCollect(collect);


        return registerDatabaseService.newStudent(student, examCycle.id);
    }

    public void confirmIntoExam(long examId, Integer studentId, Model model) {
        registerDatabaseService.studentConfirm(studentId, examId,model);
    }

    final static String examIdName="examId";
    final static String studentIdName="studentId";

    public ConfirmToken loadFromToken(String token) {
        var tokens = JWT.decode(token).getClaims();
        ConfirmToken confirmToken = new ConfirmToken();

        if(!tokens.containsKey(examIdName) || !tokens.containsKey(studentIdName))
            throw new TokenTransformFailureException("Claims Key Not Found");
        confirmToken.examId = tokens.get(examIdName).asLong();
        confirmToken.studentId = tokens.get(studentIdName).asInt();

        return confirmToken;
    }

    public String generateConfirmToken(ConfirmToken token) {
        var tokenData = JWT.create();
        tokenData.withClaim(studentIdName, token.studentId)
                .withClaim(examIdName, token.examId);

        return tokenData.sign(Algorithm.HMAC256("1141451919"));
    }


    public static class ConfirmToken {
        public Integer studentId;
        public Long examId;
    }

    public ExamFinalCollection ExamWorkUpload(MultipartFile file,
                                              Long examId,
                                              String stuId,
                                              String stuName,
                                              String stuEmail) {
        var student = registerDatabaseService.findStudentByStuIdAndStuName(stuId, stuName, stuEmail);
        var exam = registerDatabaseService.getExam(examId, GroupDepartment.lambadaDepartment());

        //检查学生是否能够参与本轮考核（有报名+通过前面全部考核）
        registerDatabaseService.checkStudentSupport(
                exam,
                registerDatabaseService.findExamCycleByIdAndDepartment(
                        exam.examCycleID,
                        GroupDepartment.lambadaDepartment()),
                student.id);

        //考核是否超时
        var nowTime= LocalDate.now();
        if (!(nowTime.isAfter(exam.startAt)&&nowTime.isBefore(exam.endAt)))
            throw new HandOnWorksOverTimeException(exam);
        //文件上传
        var work = examWorksFileStorageService.storage(file);

        //check exist
        var result = finalCollectionRepository.findByStudentIDAndExamID(student.id, exam.id);
        if (result.isPresent()) {
            var temp = result.get();
            //remove old file
            examWorksFileStorageService.delete(temp);

            temp.examFile = work.examFile;
            work = temp;
        } else {
            work.examID = exam.id;
            work.studentID = student.id;
        }

        //save
        finalCollectionRepository.save(work);

        //change status
        var status=registerDatabaseService.findExamLinkByStudentId(student.id);
        var temp=new HashMap<Long, StudentExamLink>();
        for (StudentExamLink examLink :
                status) {
            temp.put(examLink.examId, examLink);
        }
        if(!temp.containsKey(examId))
            // TODO: 2021/4/20 exam not found exception
            throw new ExamCycleNotFoundException(examId,GroupDepartment.lambadaDepartment());
        var stuStatus=temp.get(examId);
        stuStatus.status= ExamStatus.ASSESS;

        registerDatabaseService.examLinkRepository.save(stuStatus);
        return work;
    }
}
