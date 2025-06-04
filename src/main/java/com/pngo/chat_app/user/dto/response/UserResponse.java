package com.pngo.chat_app.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String phone;
    String email;
    String firstName;
    String middleName;
    String lastName;
    String avatarUrl;

}
