package com.jacky.register.models.respond.department;

import java.io.Serializable;
import java.util.List;

public class DepartmentInformation implements Serializable {
    public String name;
    public String information;

    public List<AdminInformation>admins;
}
