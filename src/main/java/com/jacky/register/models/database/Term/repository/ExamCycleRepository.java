package com.jacky.register.models.database.Term.repository;

import com.jacky.register.models.database.Term.ExamCycle;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamCycleRepository extends JpaRepository<ExamCycle,Long> {

    Optional<ExamCycle>findByIdAndDepartmentID(Long id,Integer departmentId);
    List<ExamCycle> findByDepartmentID(Integer departmentId);

    void deleteByIdAndDepartmentID(Long id,Integer departmentId);

}
