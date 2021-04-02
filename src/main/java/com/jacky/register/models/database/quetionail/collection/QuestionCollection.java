package com.jacky.register.models.database.quetionail.collection;

import com.jacky.register.models.database.quetionail.Questionable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(
        indexes = {
                @Index(columnList = "submit_at")
        }
)
public class QuestionCollection {
    @Id
    @GeneratedValue
    Integer id;

    @CreatedDate
    @Column(name = "submit_at")
    public
    LocalDateTime submitAt;

    @OneToMany()
    public
    Set<CollectionItem> items;

    @ManyToOne
    public Questionable question;
}
