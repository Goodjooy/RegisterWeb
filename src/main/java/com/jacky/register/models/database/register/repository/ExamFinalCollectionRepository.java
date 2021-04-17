package com.jacky.register.models.database.register.repository;

import com.jacky.register.models.database.register.registerCollection.ExamFinalCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamFinalCollectionRepository extends JpaRepository<ExamFinalCollection,Long> {
}
