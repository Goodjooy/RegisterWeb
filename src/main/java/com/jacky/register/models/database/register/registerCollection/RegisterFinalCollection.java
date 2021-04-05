package com.jacky.register.models.database.register.registerCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//考核终了收集表
@Entity
public class RegisterFinalCollection {
    @Id
    @GeneratedValue
    Long id;

    //学生id；
    @Column(nullable = false,unique = true,length = 20)
    String studentID;

    //考核轮ID
    @Column(nullable = false)
    Long termID;

    //考核提交文件路径
    @Column(nullable = false,length = 128)
    String examFile;

}
