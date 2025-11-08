package com.scb.supplychainbrief.common.service;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.util.Role;

import java.util.List;

public interface UserService {
    UserDto.UserResponse createUser(UserDto.UserRequest request); // US1
    UserDto.UserResponse updateUserRole(Long id, Role role); // US2
    List<UserDto.UserResponse> getAllUsers();
    UserDto.UserResponse findByEmail(String email); // For AOP
}
