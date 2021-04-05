package com.jacky.register.models.database.register.registerCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 考核开始时报名
 */
@Entity
public class StudentTermParentLink {
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long studentID;

    @Column(nullable = false)
    Long termParentID;
}
