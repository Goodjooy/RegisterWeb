package com.jacky.register.models.request.department;

import com.jacky.register.dataHandle.dataCheck.annonace.Nullable;

import java.io.Serializable;

public class DepartmentCreate implements Serializable {
    @Nullable()
    public String name;
    @Nullable(nullable = true)
    public String info;

}
