package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentExamLinkRepository extends JpaRepository<StudentExamLink,Long> {
    List<StudentExamLink> findByExamId(Long id);
   // List<StudentExamLink> findAllByExamId(Iterable<Long> ids);
    Set<StudentExamLink> findByStudentID(Integer id);

    long countByStudentIDAndExamId(Integer stuId,Long examId);
    Optional<StudentExamLink>  findByStudentIDAndExamId(Integer stuId,Long examId);

    //删除学生连接
    void deleteByStudentIDAndExamId(Integer stuId,Long examId);
    //删除考核轮
    void deleteAllByExamId(long examId);

    //获取大于给定exam的连接
    List<StudentExamLink>findByStudentIDAndExamIdGreaterThan(int  stuId,long examId);

}
