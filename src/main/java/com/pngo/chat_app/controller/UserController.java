package com.pngo.chat_app.controller;

import com.pngo.chat_app.entity.User;
import com.pngo.chat_app.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping("/test")
    public List<User> test() {
        return userService.getAllUser();
    }

    @GetMapping("/user")
    String getUser() {
        return "Hello, User!";
    }

}
