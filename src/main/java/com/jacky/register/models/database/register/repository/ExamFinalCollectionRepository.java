package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.registerCollection.ExamFinalCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public interface ExamFinalCollectionRepository extends JpaRepository<ExamFinalCollection,Long> {
    //find by stu id and exam id
    Optional<ExamFinalCollection>findByStudentIDAndExamID(Integer stuId,Long examId);

    //删除学生考核提交信息
    void deleteByStudentIDAndExamID(Integer stuId ,Long examId);
}
