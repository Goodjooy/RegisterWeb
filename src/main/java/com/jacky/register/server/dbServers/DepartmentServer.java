package com.jacky.register.server.dbServers;

import com.jacky.register.err.Dpartment.notFound.DepartAdminTypeNotFoundException;
import com.jacky.register.err.Dpartment.notFound.DepartmentNotFoundException;
import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.users.AdminRepository;
import com.jacky.register.models.database.users.Administer;
import com.jacky.register.models.database.users.SuperAdmin;
import com.jacky.register.models.request.department.DepartmentCreate;
import com.jacky.register.models.request.user.AdminCreate;
import com.jacky.register.models.respond.department.AdminInformation;
import com.jacky.register.models.respond.department.DepartmentInformation;
import com.jacky.register.security.DatabaseEntityUserDetails;
import com.jacky.register.server.dbServers.user.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServer {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    AdminService adminService;
    @Autowired
    AdminRepository adminRepository;

    public GroupDepartment getDepartmentByID(Integer id) {
        var result = departmentRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new DepartmentNotFoundException(id);
        }
    }

    public GroupDepartment getNowDepartment() {
        return getFirstDepartment();
    }

    public GroupDepartment getFirstDepartment() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        DatabaseEntityUserDetails<?>
                userDetails = (DatabaseEntityUserDetails<?>) auth
                .getPrincipal();

        Optional<GroupDepartment> result;
        if (userDetails.getModel() instanceof SuperAdmin && userDetails.getDepartmentId() != -1) {
            result = departmentRepository.findById(userDetails.getDepartmentId());
            if (result.isPresent()) {
                return result.get();
            }
        } else if (userDetails.getModel() instanceof Administer) {
            //不是超级管理员
            return ((Administer) userDetails.getModel()).groupIn;
        } else
            throw new DepartAdminTypeNotFoundException(userDetails.getModel().getClass());
        throw new DepartmentNotFoundException(userDetails.getDepartmentId());
    }
    public DepartmentInformation toRespond(GroupDepartment department){
        DepartmentInformation information=new DepartmentInformation();

        information.information=department.information;
        information.name= department.name;

        information.admins=department.administers.stream().map(
                administer -> {
                    AdminInformation information1=new AdminInformation();
                    information1.email=administer.email;
                    information1.name= administer.name;

                    return information1;
                }
        ).collect(Collectors.toList());

        return information;
    }

    public GroupDepartment newDepartment(DepartmentCreate create){
        GroupDepartment department=new GroupDepartment();

        department.name= create.name;
        department.information= create.info;

        return departmentRepository.save(department);

    }
    public GroupDepartment resetDepartmentInformation(GroupDepartment department,String information){
        department.information=information;

        return departmentRepository.save(department);
    }

    public GroupDepartment resetDepartmentName(GroupDepartment department,String name){
        department.name=name;
        return departmentRepository.save(department);
    }

    public Administer addAdminToDepartment(AdminCreate adminCreate){
        var department=getDepartmentByID(adminCreate.targetDepartmentId);

        return adminService.newAdminister(adminCreate.data, department);
    }

}
