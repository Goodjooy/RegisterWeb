package com.jacky.register.models.database.group;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<GroupDepartment,Integer> {
}
