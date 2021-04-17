package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student,Integer> {
}
