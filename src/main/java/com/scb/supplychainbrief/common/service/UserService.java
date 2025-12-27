package com.scb.supplychainbrief.common.service;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.model.User;
import com.scb.supplychainbrief.common.util.Role;
import org.springframework.security.core.userdetails.UserDetailsService; // <--- IMPORT THIS
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserService extends UserDetailsService {

    UserDto.UserResponse createUser(UserDto.UserRequest request);

    UserDto.UserResponse updateUserRole(Long id, Role role);

    List<UserDto.UserResponse> getAllUsers();

    @Override
    User loadUserByUsername(String email) throws UsernameNotFoundException;

    void updateRefreshToken(User user, String refreshToken);
}