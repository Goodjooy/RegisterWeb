package com.jacky.register.models.database.register.repository;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.jacky.register.models.database.register.Student;
import org.apache.juli.logging.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student,Integer> {

    Optional<Student> findByNameAndStuIdAndEmail(String name, String stuId, String email);

}
