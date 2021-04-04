package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.choices.SelectSort;
import com.jacky.register.models.database.quetionail.subItems.ItemSort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionItemRepository extends JpaRepository<CollectionItem,Integer> {
     Long countByDataAndItem(String data, ItemSort sort);
}
