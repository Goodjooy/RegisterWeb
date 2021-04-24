package com.jacky.register.server.dbServers;

import com.jacky.register.err.Dpartment.notFound.DepartAdminTypeNotFoundException;
import com.jacky.register.err.Dpartment.notFound.DepartmentNotFoundException;
import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.users.AdminRepository;
import com.jacky.register.models.database.users.Administer;
import com.jacky.register.models.database.users.SuperAdmin;
import com.jacky.register.security.DatabaseEntityUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentServer {
    @Autowired
    DepartmentRepository departmentRepository;

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
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var adminEmail = auth.getName();
        //todo 用户级别认证


        return null;
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

}
