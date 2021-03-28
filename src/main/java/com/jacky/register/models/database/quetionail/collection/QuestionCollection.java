package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.Questionable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
public class QuestionCollection {
    @Id
    @GeneratedValue
    Integer id;

    @CreatedDate
            public
    LocalDateTime submitAt;

    @OneToMany()
            public
    Set<CollectionItem>items;

    @ManyToOne
    public Questionable question;
}
