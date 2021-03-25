package com.jacky.register.server.dbServers;

import com.jacky.register.err.RowNotFound;
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
            throw new RowNotFound("id==`"+id+"` not found in `group_department` table");
        }

    }
}
