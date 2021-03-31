package com.jacky.register.models.database.quetionail.subItems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemSortRepository extends JpaRepository<ItemSort,Integer> {
    public Optional<ItemSort>findIByQuestionIDAndSortIndex(Integer questionID,Integer sortIndex);

}
