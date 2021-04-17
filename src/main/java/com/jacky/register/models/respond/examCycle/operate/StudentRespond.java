package com.jacky.register.models.respond.examCycle.operate;

import com.jacky.register.models.database.register.Student;

import java.io.Serializable;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class StudentRespond implements Serializable {
    public Integer id;

    public String name;
    public String email;
    public String qqId;
    public String studentId;
    public String phone;

    public List<StudentStatusRespond>status;
}
