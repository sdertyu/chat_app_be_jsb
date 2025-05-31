package com.pngo.chat_app.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NotNull
public class UserSignup {

    private String phone;

    @Email
    private String email;

    @Size(min = 8, max = 100)
    private String password;

    private String firstName;

    private String middleName;

    private String lastName;

    private String avatarUrl;

}
