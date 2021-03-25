package com.jacky.register.models.database.quetionail;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface QuestionRepository extends JpaRepository<Questionable, Integer> {
}
