package com.scb.supplychainbrief.common.service;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.mapper.UserMapper;
import com.scb.supplychainbrief.common.model.User;
import com.scb.supplychainbrief.common.repository.UserRepository;
import com.scb.supplychainbrief.common.util.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // Injected from SecurityConfig

    @Override
    public UserDto.UserResponse createUser(UserDto.UserRequest request) {
        // US1: "créer un compte utilisateur avec un rôle spécifique"
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use: " + request.getEmail());
        }

        User user = userMapper.toUser(request);
        // --- HASH THE PASSWORD ---
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserDto.UserResponse updateUserRole(Long id, Role role) {
        // US2: "modifier le rôle d'un utilisateur existant"
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setRole(role);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public List<UserDto.UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto.UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
        return userMapper.toUserResponse(user);
    }
}
