package com.jacky.register;

import com.jacky.register.config.StorageConfig;
import com.jacky.register.models.database.group.DepartmentRepository;
import com.jacky.register.models.database.group.GroupDepartment;
import com.jacky.register.models.database.users.SuperAdmin;
import com.jacky.register.models.database.users.SuperUserRepository;
import com.jacky.register.server.localFiles.StorageService;
import io.lettuce.core.dynamic.annotation.Command;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableConfigurationProperties(StorageConfig.class)
@EnableRedisHttpSession
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(SuperUserRepository repository, PasswordEncoder encoder, DepartmentRepository departmentRepository){
		return args -> {


			GroupDepartment department=new GroupDepartment();
			department.information="测试部门";
			department.name="测试部门";

			var resultD=departmentRepository.findAll();
			if(resultD.isEmpty()){
				departmentRepository.save(department);
			}
			SuperAdmin admin=new SuperAdmin();
			admin.name="temp";
			admin.email="964413011@qq.com";

			var result=repository.findOne(Example.of(admin));
			if (result.isEmpty()){
				admin.password=encoder.encode("123456");
				repository.save(admin);
			}
		};
	}

}
