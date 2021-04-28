package com.jacky.register.server.dbServers.register;

import com.jacky.register.err.register.notFound.ExamCycleNotFoundException;
import com.jacky.register.err.register.notFound.RegisterQuestionNotFoundException;
import com.jacky.register.err.register.notFound.StudentNotFoundException;
import com.jacky.register.err.register.requireNotSatisfy.StudentNotPassAllPreExamException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.Term.repository.ExamCycleRepository;
import com.jacky.register.models.database.Term.repository.ExamRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.RegisterQuestion;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.ExamFinalCollection;
import com.jacky.register.models.database.register.registerCollection.StudentExamCycleLink;
import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import com.jacky.register.models.database.register.repository.*;
import com.jacky.register.models.status.ExamCycleStatus;
import com.jacky.register.models.status.ExamStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegisterDatabaseService {
    @Autowired
    ExamCycleRepository examCycleRepository;
    @Autowired
    ExamRepository examRepository;

    @Autowired
    StudentExamCycleLinkRepository examCycleLinkRepository;
    @Autowired
    StudentExamLinkRepository examLinkRepository;
    @Autowired
    ExamFinalCollectionRepository examFinalCollectionRepository;


    @Autowired
    RegisterQuestionRepository registerQuestionRepository;
    @Autowired
    StudentRepository studentRepository;

    public ExamCycle findExamCycleByIdAndDepartment(Long id, GroupDepartment department) {
        var examCycle = getExamCycle(id, department);
        examCycle.examList = findExamByExamCycleId(id);
        examCycle.studentSet = findStudentInExamCycle(id);
        examCycle.question=findRegisterQuestionById(examCycle.registerQuestionID);
        return examCycle;
    }

    public Exam findExamById(long id) {
        var exam = getExam(id, GroupDepartment.lambadaDepartment());

        exam.termStudents = findStudentInExam(id);

        return exam;
    }

    public List<Exam> findExamByExamCycleId(long id) {
        var exams = examRepository.findAllByExamCycleID(id);

        exams = exams.stream().peek(exam -> {
            var examStudents = examLinkRepository.findByExamId(exam.id);
            exam.termStudents = findStudentById(examStudents.stream().map(s -> s.studentID)
                    .collect(Collectors.toList()));
        }).collect(Collectors.toList());
        return exams;
    }

    public Set<StudentExamLink> findExamLinkByStudentId(int id) {
        return examLinkRepository.findByStudentID(id);
    }


    public ExamCycleStatus getStudentExamCycleStatus(Student student, long examCycleId) {
        return findStudentExamCycleLinkByStudentAndExamCycleID(student, examCycleId).status;
    }

    public ExamCycleStatus getStudentExamCycleStatus(int student, long examCycleId) {
        return findStudentExamCycleLinkByStudentAndExamCycleID(student, examCycleId).status;
    }

    public void setExamCycleStatus(Student student, long examCycle, ExamCycleStatus status) {
        var linker = findStudentExamCycleLinkByStudentAndExamCycleID(student, examCycle);
        linker.status = status;

        examCycleLinkRepository.save(linker);
    }

    public List<StudentExamLink> findExamLinkByExamId(Long id) {
        return examLinkRepository.findByExamId(id);
    }

    public RegisterQuestion findRegisterQuestionById(long id) {
        var result = registerQuestionRepository.findById(id);
        if (result.isEmpty())
            throw new RegisterQuestionNotFoundException(id);

        return result.get();
    }

    public Set<Student> findStudentInExamCycle(long id) {
        var examCycleLinks = examCycleLinkRepository.findByExamCycleId(id);
        return findStudentById(
                examCycleLinks.stream().map(s -> s.studentID).collect(Collectors.toList())
        );
    }

    public Set<Student> findKeyWordStudentInExamCycle(long id, String keyword) {
        var students = findStudentInExamCycle(id);

        return searchStudentWithKeyword(students, keyword);
    }

    public String getStudentExamWorksName(Long examId, Integer studentID) {
        return getStudentExamWorks(examId,studentID).examFile;
    }
    public ExamFinalCollection getStudentExamWorks(Long examId, Integer studentID) {
        var result = examFinalCollectionRepository.findByStudentIDAndExamID(studentID, examId);
        if (result.isEmpty())
            return ExamFinalCollection.nullWorks(studentID,examId);
        return result.get();
    }

    public ExamStatus getStudentExamStatues(Long examId, Integer stuId) {
        var result = examLinkRepository.findByStudentIDAndExamId(stuId, examId);
        if (result.isEmpty())
            throw new StudentNotFoundException(stuId);
        return result.get().status;
    }

    public Set<Student> findStudentInExam(long id) {
        var examCycleLinks = examLinkRepository.findByExamId(id);
        return findStudentById(
                examCycleLinks.stream().map(s -> s.studentID).collect(Collectors.toList())
        );
    }

    public Set<Student> searchStudentInExam(long id, String keyword) {
        var students = findStudentInExam(id);
        return searchStudentWithKeyword(students, keyword);
    }

    public Student newStudent(Student student, Long examCycleId) {
        return newStatusStudent(student, examCycleId, ExamCycleStatus.REGISTER);
    }

    public Student newStatusStudent(Student student, Long examCycleId, ExamCycleStatus status) {
        //check student exist;
        var result = studentRepository
                .findByNameAndStuIDAndEmail(student.name, student.stuID, student.email);
        if (result.isEmpty())
            student = studentRepository.save(student);
        else {
            student = result.get();
        }
        //examCycle - student Linker
        StudentExamCycleLink examCycleLink = new StudentExamCycleLink();

        examCycleLink.studentID = student.id;
        examCycleLink.examCycleId = examCycleId;
        examCycleLink.status = status;

        examCycleLinkRepository.save(examCycleLink);

        return student;
    }

    Set<Student> searchStudentWithKeyword(Set<Student> students, String keyword) {
        return students.stream()
                .filter(
                        student -> student.name.contains(keyword) ||
                                student.stuID.contains(keyword)
                )
                .collect(Collectors.toSet());
    }

    public StudentExamLink getExamLinkByStudentIdAndExamId(Integer stuId, Long examId) {
        var result = examLinkRepository.findByStudentIDAndExamId(stuId, examId);
        if (result.isEmpty())
            throw new StudentNotFoundException(stuId);
        return result.get();
    }

    public void studentConfirm(Integer stuId, Long examId) {
        var exam = getExam(examId, GroupDepartment.lambadaDepartment());
        var examCycle = findExamCycleByIdAndDepartment(exam.examCycleID, GroupDepartment.lambadaDepartment());
        RegisterOperationService.checkExamCycle(examCycle);
        var students =
                findStudentInExamCycle(exam.examCycleID).stream().map(student -> student.id)
                        .collect(Collectors.toSet());
        if (!students.contains(stuId))
            throw new StudentNotFoundException(stuId);

        //生成student 和exam link
        StudentExamLink examLink = new StudentExamLink();

        checkStudentSupport(exam, examCycle, stuId);

        if (examLinkRepository.countByStudentIDAndExamId(stuId, examId) == 0) {
            examLink.studentID = stuId;
            examLink.examId = exam.id;
            examLink.status = ExamStatus.REGISTER;

            examLinkRepository.save(examLink);
        }
    }

    Student findStudentById(Integer id) {
        var result = studentRepository.findById(id);
        if (result.isEmpty())
            throw new StudentNotFoundException(id);
        return result.get();
    }

    Student findStudentByStuIdAndStuName(String stuId, String stuName, String stuEmail) {
        var result = studentRepository.findByNameAndStuIDAndEmail(stuName, stuId, stuEmail);
        if (result.isEmpty())
            throw new StudentNotFoundException(stuId, stuName, stuEmail);
        return result.get();
    }

    Set<Student> findStudentById(List<Integer> ids) {
        Set<Student> students = new HashSet<>(studentRepository.findAllById(
                ids
        ));
        students = students.stream().peek(student -> {
            var examLinks = examLinkRepository.findByStudentID(student.id);
            var exams = examRepository.findAllById(examLinks.stream().map(s -> s.examId).collect(Collectors.toSet()));

            student.studentExam = new HashSet<>(exams);
        })
                .collect(Collectors.toSet());
        return students;
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

    public Exam getExam(Long id, GroupDepartment department) {
        Optional<Exam> result;
        result = examRepository.findById(id);


        if (result.isEmpty())
            throw new ExamCycleNotFoundException(id, department);
        return result.get();
    }

    List<StudentExamLink> getAllExamLink(Collection<Long> ids) {
        return examLinkRepository.findAllById(ids);
    }

    void checkStudentSupport(Exam exam, ExamCycle examCycle, int studentId) {

        var examCycleStatus = getStudentExamCycleStatus(studentId, examCycle.id);

        //判断之前考核是否通过
        var previewExams = examCycle.examList.stream().filter(exam1 -> exam1.id < exam.id)
                .map(exam1 -> exam1.id)
                .sorted(Comparator.comparing(Long::longValue))
                .collect(Collectors.toList());
        //之前的考核连接
        var examLinks = getAllExamLink(previewExams);
        //之前考核未通过
        if (examLinks.stream().filter(examLink1 -> examLink1.status.canBeContinue()).count() != examLinks.size() ||
                examCycleStatus.canBeContinue())
            throw new StudentNotPassAllPreExamException(studentId);
    }

    public StudentExamCycleLink findStudentExamCycleLinkByStudentAndExamCycleID(Student student, long examCycleId) {
        return findStudentExamCycleLinkByStudentAndExamCycleID(student.id, examCycleId);
    }

    public StudentExamCycleLink findStudentExamCycleLinkByStudentAndExamCycleID(int student, long examCycleId) {
        var result = examCycleLinkRepository
                .findByExamCycleIdAndStudentID(examCycleId, student);
        if (result.isEmpty())
            throw new StudentNotFoundException(student);

        return result.get();
    }


}
