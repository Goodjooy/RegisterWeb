package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.registerCollection.StudentExamCycleLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentExamCycleLinkRepository extends JpaRepository<StudentExamCycleLink ,Long> {
    List<StudentExamCycleLink> findByExamCycleId(long id);
    Optional<StudentExamCycleLink>findByExamCycleIDAndStudentId(long id,int stuId);
}
