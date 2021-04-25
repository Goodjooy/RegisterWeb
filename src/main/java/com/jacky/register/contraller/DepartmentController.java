package com.jacky.register.contraller;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.request.department.DepartmentCreate;
import com.jacky.register.models.request.user.AdminCreate;
import com.jacky.register.models.respond.department.DepartmentInformation;
import com.jacky.register.server.dbServers.DepartmentServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//超级管理员控制部门的额外类
//部门名称，部门管理员分配
@RestController
@RequestMapping("/api/department/operation")
public class DepartmentController {
    @Autowired
    DepartmentServer departmentServer;
    @GetMapping("/department")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER')")
    public Result<DepartmentInformation>departmentData(
            @RequestParam("id")int id
    ){
        var department=departmentServer.getDepartmentByID(id);
        var result=departmentServer.toRespond(department);

        return Result.okResult(result);
    }

    @PostMapping("/department")
    @PreAuthorize("hasRole('ROLE_SUPER')")
    public Result<Integer> newDepartment(
            @RequestBody DepartmentCreate departmentCreate
    ) {
        var department = departmentServer.newDepartment(departmentCreate);

        return Result.okResult(department.ID);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ROLE_SUPER')")
    public Result<Integer> newAdminIntoDepartment(
            @RequestBody AdminCreate adminCreate
    ) {
        var admin = departmentServer.addAdminToDepartment(adminCreate);

        return Result.okResult(admin.ID);
    }

    @PutMapping("/department/information")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
    public Result<Integer> updateInformation(
            @RequestParam("info") String information
    ) {
        var department = departmentServer.getFirstDepartment();
        department = departmentServer.resetDepartmentInformation(department, information);

        return Result.okResult(department.ID);
    }

    @PutMapping("/department/name")
    @PreAuthorize("hasRole('ROLE_SUPER')")
    public Result<Integer> updateName(
            @RequestParam("name") String name
    ) {
        var department = departmentServer.getFirstDepartment();
        department = departmentServer.resetDepartmentName(department, name);

        return Result.okResult(department.ID);
    }
}
