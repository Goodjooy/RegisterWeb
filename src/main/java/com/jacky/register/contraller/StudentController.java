package com.jacky.register.contraller;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.request.register.student.StudentCreate;
import com.jacky.register.models.request.register.student.StudentUpdate;
import com.jacky.register.server.dbServers.register.RegisterCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/student/operation")
public class StudentController {
    @Autowired
    RegisterCollectionService collectionService;

    //新建学生
    @PreAuthorize("hasAnyRole('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping("/student")
    public Result<Integer> newStudent(
            @RequestBody StudentCreate create
    ) {
        var student = collectionService.createStudent(create.data);
        return Result.okResult(student.id);
    }

    //修改学生信息
    @PreAuthorize("hasRole('ROLE_SUPER')")
    @PutMapping("/student")
    public Result<Integer> newStudent(
            @RequestBody StudentUpdate create
    ) {
        var student = collectionService.updateStudentInfo(create.data, create.id);
        return Result.okResult(student.id);
    }
}
