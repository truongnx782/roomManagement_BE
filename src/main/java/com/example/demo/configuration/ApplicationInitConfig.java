package com.example.demo.configuration;

import com.example.demo.entity.Company;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.User_Role;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.User_RoleRepository;
import com.example.demo.util.ERole;
import com.example.demo.util.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";
    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        User_RoleRepository user_roleRepository,
                                        CompanyRepository companyRepository) {
        log.info("Initializing application.....");
        return args -> {

            if(companyRepository.findByCompanyCode("ADMIN").isEmpty()){
                Company company = new Company();
                company.setCompanyCode("ADMIN");
                company.setCompanyName("ADMIN");
                company.setStatus(Utils.ACTIVE);
                companyRepository.save(company);
            }

            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {

                if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()){
                    Role role = new  Role();
                    role.setName(ERole.ROLE_ADMIN);
                    roleRepository.save(role);
                    log.warn("ADMIN Role has been created!");
                }
                if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()){
                    Role role = new  Role();
                    role.setName(ERole.ROLE_USER);
                    roleRepository.save(role);
                    log.warn("ADMIN Role has been created!");
                }

              Optional<Company> company =companyRepository.findByCompanyCode("ADMIN");

                Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .status(Utils.ACTIVE)
                        .companyId(company.get().getId())
                        .build();
               user= userRepository.save(user);

                User_Role user_role = new User_Role();
                user_role.setUserId(user);
                user_role.setRoleId(role.get());
                user_role.setCompanyId(company.get().getId());
                user_role.setStatus(Utils.ACTIVE);
                user_roleRepository.save(user_role);

                log.warn("admin user has been created with default password: admin, please change it");
            }

            log.info("Application initialization completed ....."); // Ghi log thông báo khởi tạo ứng dụng hoàn tất
        };
    }
}
