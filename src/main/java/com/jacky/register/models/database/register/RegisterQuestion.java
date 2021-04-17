package com.jacky.register.models.database.register;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
//报名表
public class RegisterQuestion {
    @Id
    @GeneratedValue
    public Long id;

    //绑定问题
    @Column(nullable = false)
    public Integer questionID;

    //问卷信息绑定
    @Column(nullable = false)
    public Integer studentItemID;
    @Column(nullable = false)
    public Integer studentNameItemID;
    @Column(nullable = false)
    public Integer emailItemID;
    @Column(nullable = true)
    public Integer qqItemID;
    @Column(nullable = true)
    public Integer phoneItemID;
}
