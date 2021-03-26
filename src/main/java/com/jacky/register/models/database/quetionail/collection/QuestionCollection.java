package com.jacky.register.models.database.quetionail.collection;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
public class QuestionCollection {
    @Id
    @GeneratedValue
    Integer id;

    @CreatedDate
    LocalDateTime submitAt;

    @OneToMany()
    Set<CollectionItem>items;
}
