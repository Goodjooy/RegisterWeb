package com.jacky.register.server.dbServers;

import com.jacky.register.err.Dpartment.notFound.DepartmentNotFoundException;
import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServer {
    @Autowired
    DepartmentRepository departmentRepository;

    public GroupDepartment getDepartmentByID(Integer id){
        var result= departmentRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }else {
            throw new DepartmentNotFoundException(id);
        }
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
