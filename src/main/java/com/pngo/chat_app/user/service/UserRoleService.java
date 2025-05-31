package com.pngo.chat_app.user.service;


import com.pngo.chat_app.user.model.UserRole;
import com.pngo.chat_app.user.repository.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserRoleService {

    UserRoleRepository repository;

    public void addUserRole(int userId, int roleId) {
        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        repository.save(userRole);
    }
}
