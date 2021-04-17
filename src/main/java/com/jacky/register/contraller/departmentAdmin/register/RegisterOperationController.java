package com.jacky.register.contraller.departmentAdmin.register;

import com.jacky.register.dataHandle.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//报名表建立的控制器
@RestController
@RequestMapping("/api/register/operate")
public class RegisterOperationController {

    public Result<?> getRegister(
            Long id
    ){
        return null;
    }
}
