package com.jacky.register.contraller;

import com.jacky.register.dataHandle.Result;
import com.jacky.register.models.request.department.DepartmentCreate;
import com.jacky.register.models.request.user.AdminCreate;
import com.jacky.register.models.respond.department.DepartmentInformation;
import com.jacky.register.server.dbServers.DepartmentServer;
import com.jacky.register.server.dbServers.user.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

//超级管理员控制部门的额外类
//部门名称，部门管理员分配
@RestController
@RequestMapping("/api/department/operation")
public class DepartmentController {
    @Autowired
    DepartmentServer departmentServer;
    @Autowired
    AdminService adminService;

    @GetMapping("/department/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER')")
    public Result<Stream<DepartmentInformation>> allDepartmentData(
    ) {
        var departments = departmentServer.getAllDepartment()
                .stream()
                .map(departmentServer::toRespond);

        return Result.okResult(departments);
    }

    @GetMapping("/department")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER')")
    public Result<DepartmentInformation> departmentData(
            @RequestParam("id") int id
    ) {
        var department = departmentServer.getDepartmentByID(id);
        var result = departmentServer.toRespond(department);

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

    @DeleteMapping("/department")
    @PreAuthorize("hasRole('ROLE_SUPER')")
    public Result<Integer> removeDepartment(
            @RequestParam("id") Integer id
    ) {
        var department = departmentServer.getDepartmentByID(id);
        departmentServer.removeDepartment(department);

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

    @DeleteMapping("/admin")
    @PreAuthorize("hasRole('ROLE_SUPER')")
    public Result<Integer> removeAdmin(
            @RequestParam("id") Integer id
    ) {
        adminService.removeAdminister(id);
        return Result.okResult(id);
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

    @PostMapping("/switch")
    public ResponseEntity<?> setDepartment(
            @RequestParam("id")Integer departmentId
    ){
        return ResponseEntity
                .ok()
                .header("Set-Cookie",String.format("DPMIN_INDEX_VAL=%s; Path=/;",departmentId))
                .build();
    }
}
