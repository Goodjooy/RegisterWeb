package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.registerCollection.StudentExamLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StudentExamLinkRepository extends JpaRepository<StudentExamLink,Long> {
    List<StudentExamLink> findByExamId(Long id);
   // List<StudentExamLink> findAllByExamId(Iterable<Long> ids);
    Set<StudentExamLink> findByStudentID(Integer id);
}
