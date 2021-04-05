package com.jacky.register.models.database.Term;

import com.jacky.register.models.database.register.Student;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * 考核系统
 *
 * 由管理员控制
 *
 * 可以新建考核轮
 * 保留报名的全部学生信息
 * 考核发起部门
 *
 */
@Entity
//总考核系统
// 管理员新建考核
public class TermParent {
    @Id
    @GeneratedValue
    Long id;

    //考核发起部门
    @Column(nullable = false)
    Long departmentID;

    //全部考核轮
    @Transient
    List<Term>termList;

    //全部考核学生
    @Transient
    Set<Student>studentSet;

}
