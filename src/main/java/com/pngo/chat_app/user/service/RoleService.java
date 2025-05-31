package com.pngo.chat_app.user.service;

import com.pngo.chat_app.user.model.Role;
import com.pngo.chat_app.common.enums.RoleEnum;
import com.pngo.chat_app.user.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService {

    RoleRepository repository;

    public void checkInitRole() {
        boolean check = !repository.findAll().isEmpty(); //check role initialization
        log.warn(String.format("Role initialization check: %s", check));
        if (!check) {
            initRole();
        }
    }

    public void initRole() {
        RoleEnum [] roles = RoleEnum.values();
        Role newRole = null;
        for (RoleEnum role : roles) {
            newRole = Role.builder()
                    .name(role.name())
                    .build();

            repository.save(newRole);
        }
    }

}
