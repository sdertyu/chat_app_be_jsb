package com.pngo.chat_app.common.configuration;

import com.pngo.chat_app.user.model.User;
import com.pngo.chat_app.common.enums.RoleEnum;
import com.pngo.chat_app.user.repository.UserRepository;
import com.pngo.chat_app.user.service.RoleService;
import com.pngo.chat_app.user.service.UserRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleService roleService;
    UserRoleService userRoleService;


    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            //check role init
            roleService.checkInitRole();


            if (userRepository.findByEmail("admin").isEmpty()) {
//                HashSet<String> roles = new HashSet<>();
//                roles.add(RoleEnum.ADMIN.name());
//                String roles = RoleEnum.ADMIN.name();
                User user = User.builder()
                        .email("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();

                User newAdmin = userRepository.save(user);

                userRoleService.addUserRole(newAdmin.getId(), RoleEnum.ADMIN.ordinal() + 1);


                log.warn("Admin user created with username: admin and password: admin, please change it after first login");
            }
        };
    }

}
