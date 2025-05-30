package com.pngo.chat_app.service;

import com.pngo.chat_app.entity.User;
import com.pngo.chat_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
