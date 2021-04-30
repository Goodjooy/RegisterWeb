package com.jacky.register.server.dbServers.user;

import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.users.AdminRepository;
import com.jacky.register.models.database.users.Administer;
import com.jacky.register.models.request.user.AdminData;
import com.jacky.register.server.dbServers.DepartmentServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    PasswordEncoder encoder;

    public Administer newAdminister(AdminData data, GroupDepartment department){
        Administer administer=new Administer();

        administer.email= data.email;
        administer.name= data.name;
        administer.password=encoder.encode(data.password);

        administer.groupIn=department;
        adminRepository.save(administer);

        department.administers.add(administer);
        departmentRepository.save(department);

        return administer;
    }

    public void removeAdminister(int id){
        var result=adminRepository.findById(id);
        result.ifPresent(administer -> adminRepository.delete(administer));
    }
    // TODO: 2021/4/29 find back password

}
