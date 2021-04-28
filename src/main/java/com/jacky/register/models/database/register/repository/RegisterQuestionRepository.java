package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.RegisterQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterQuestionRepository extends JpaRepository<RegisterQuestion,Long> {

}
