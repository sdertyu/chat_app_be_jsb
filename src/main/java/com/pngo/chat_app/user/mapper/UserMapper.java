package com.pngo.chat_app.user.mapper;

import com.pngo.chat_app.user.dto.request.UserSignup;
import com.pngo.chat_app.user.dto.response.UserResponse;
import com.pngo.chat_app.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User signupToUser(UserSignup request);

    UserResponse userToUserResponse(User user);
//    void updateUserFromDto(@MappingTarget User user, UserUpdate request);

//    @Mapping(source = "firstName", target = "lastName")
//    @Mapping(target = "lastName", ignore = true)
//    UserResponse toUserResponse(User user);
}
