package com.example.demo.configuration;



import com.example.demo.Entity.Company;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repo.CompanyRepository;
import com.example.demo.Repo.RoleRepository;
import com.example.demo.Repo.UserRepository;
import com.example.demo.Util.ERole;
import com.example.demo.Util.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    // Đối tượng PasswordEncoder được sử dụng để mã hóa mật khẩu
    PasswordEncoder passwordEncoder;

    // Tên đăng nhập và mật khẩu mặc định cho người dùng admin
    @NonFinal
    static final String ADMIN_USER_NAME = "admin";
    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,

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

            // Kiểm tra xem người dùng admin đã tồn tại trong cơ sở dữ liệu chưa
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {

                // Kiểm tra và tạo vai trò ADMIN nếu chưa tồn tại
                if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()){
                    Role role = new  Role();
                    role.setName(ERole.ROLE_ADMIN);
                    role.setStatus(Utils.ACTIVE);
                    roleRepository.save(role); // Lưu vai trò vào cơ sở dữ liệu
                    log.warn("ADMIN Role has been created!"); // Ghi log thông báo vai trò ADMIN đã được tạo
                }
                if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()){
                    Role role = new  Role();
                    role.setName(ERole.ROLE_USER);
                    role.setStatus(Utils.ACTIVE);
                    roleRepository.save(role); // Lưu vai trò vào cơ sở dữ liệu
                    log.warn("ADMIN Role has been created!"); // Ghi log thông báo vai trò ADMIN đã được tạo
                }

              Optional<Company> company =companyRepository.findByCompanyCode("ADMIN");

                Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
                User user = User.builder()
                        .username(ADMIN_USER_NAME) // Tên đăng nhập của người dùng
                        .password(passwordEncoder.encode(ADMIN_PASSWORD)) // Mã hóa mật khẩu
                        .status(Utils.ACTIVE) // Trạng thái người dùng
                        .companyId(company.get().getId())
                        .build();
               user= userRepository.save(user);

//                User_Role user_role = new User_Role();
//                user_role.setUserId(user);
//                user_role.setRoleId(role.get());
//                user_role.setCompanyId(company.get().getId());
//                user_role.setStatus(Utils.ACTIVE);
//                user_roleRepository.save(user_role);

                log.warn("admin user has been created with default password: admin, please change it"); // Ghi log thông báo người dùng admin đã được tạo với mật khẩu mặc định
            }

            log.info("Application initialization completed ....."); // Ghi log thông báo khởi tạo ứng dụng hoàn tất
        };
    }
}
