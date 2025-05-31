package com.pngo.chat_app.user.service;

import com.pngo.chat_app.user.dto.request.UserSignup;
import com.pngo.chat_app.user.model.User;
import com.pngo.chat_app.common.enums.RoleEnum;
import com.pngo.chat_app.common.mapper.UserMapper;
import com.pngo.chat_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserRoleService userRoleService;

    UserMapper userMapper;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public void saveUser(UserSignup user) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userMapper.signupToUser(user);

        var saveUser = userRepository.save(newUser);

        userRoleService.addUserRole(saveUser.getId(), RoleEnum.USER.ordinal() + 1);

    }
}
