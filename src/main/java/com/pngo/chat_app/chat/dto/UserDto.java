package com.pngo.chat_app.chat.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Integer id;

    String phone;

    String email;

    String password;

    String firstName;

    String middleName;

    String lastName;

    String avatarUrl;

    Boolean isActive = true;

    Boolean isReported = false;

    Boolean isBlocked = false;

    String preferences;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;


}
