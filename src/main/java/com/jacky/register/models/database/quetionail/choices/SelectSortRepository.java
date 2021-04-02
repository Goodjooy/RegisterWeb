package com.jacky.register.models.database.quetionail.choices;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SelectSortRepository extends JpaRepository<SelectSort,Integer> {
    Optional<SelectSort> findByItemQuestionIDAndItemSortIndexAndSortIndex(int questionID, int itemID, int SelectID);
}
