package com.jacky.register.server.dbServers;

import com.jacky.register.err.Dpartment.notFound.DepartmentNotFoundException;
import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.users.AdminRepository;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServer {
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    AdminRepository adminRepository;

    public GroupDepartment getDepartmentByID(Integer id){
        var result= departmentRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }else {
            throw new DepartmentNotFoundException(id);
        }
    }
    public GroupDepartment getNowDepartment(){
        var auth=SecurityContextHolder.getContext().getAuthentication();
        var adminEmail=auth.getName();
        //todo 用户级别认证


    return  null;
    }

    public GroupDepartment getFirstDepartment(){
        var result=departmentRepository.findAll();
        if (!result.isEmpty()){
            return result.get(0);
        }else {
            throw new DepartmentNotFoundException(-1);
        }
    }

}
