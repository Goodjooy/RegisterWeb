package com.jacky.register.models.database.Term.repository;

import com.jacky.register.models.database.Term.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {

     void deleteAllByExamCycleID(Long id);
    List<Exam>findAllByExamCycleID(Long id);
}
