package com.jacky.register.server.dbServers.register;

import com.jacky.register.err.register.notFound.ExamCycleNotFoundException;
import com.jacky.register.models.database.Term.Exam;
import com.jacky.register.models.database.Term.ExamCycle;
import com.jacky.register.models.database.Term.repository.ExamCycleRepository;
import com.jacky.register.models.database.Term.repository.ExamRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.register.Student;
import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import com.jacky.register.models.database.register.repository.ExamFinalCollectionRepository;
import com.jacky.register.models.database.register.repository.StudentExamCycleLinkRepository;
import com.jacky.register.models.database.register.repository.StudentExamLinkRepository;
import com.jacky.register.models.database.register.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    StudentRepository studentRepository;

    public ExamCycle findExamCycleByIdAndDepartment(Long id, GroupDepartment department) {
        var examCycle = getExamCycle(id, department);
        // TODO: 2021/4/17 extra data;
        examCycle.examList = findExamByExamCycleId(id);
        examCycle.studentSet=findStudentInExamCycle(id);
        return examCycle;
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
    public Set<StudentExamLink>findExamLinkByStudentId(int id){
        return examLinkRepository.findByStudentID(id);
    }

    public Set<Student> findStudentInExamCycle(long id) {
        var examCycleLinks = examCycleLinkRepository.findByExamCycleId(id);
        var students = findStudentById(
                examCycleLinks.stream().map(s -> s.studentID).collect(Collectors.toList())
        );
        return students;
    }

    Set<Student> findStudentById(List<Integer>ids){
        Set<Student> students = new HashSet<>(studentRepository.findAllById(
                ids
        ));
        students = students.stream().peek(student -> {
            var examLinks=examLinkRepository.findByStudentID(student.id);
            var exams=examRepository.findAllById(examLinks.stream().map(s->s.examId).collect(Collectors.toSet()));

            student.studentExam= new HashSet<>(exams);
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

    Exam getExam(Long id, GroupDepartment department) {
        Optional<Exam> result;
        result = examRepository.findById(id);


        if (result.isEmpty())
            throw new ExamCycleNotFoundException(id, department);
        return result.get();
    }
}
