package com.scb.supplychainbrief.common.mapper;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto.UserResponse toUserResponse(User user);
    User toUser(UserDto.UserRequest request);
}
