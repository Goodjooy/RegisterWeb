package com.jacky.register.models.database.quetionail.subItems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSortRepository extends JpaRepository<ItemSort,Integer> {
}
