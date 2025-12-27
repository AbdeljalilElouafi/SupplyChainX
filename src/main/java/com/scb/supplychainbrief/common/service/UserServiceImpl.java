package com.scb.supplychainbrief.common.service;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.mapper.UserMapper;
import com.scb.supplychainbrief.common.model.User;
import com.scb.supplychainbrief.common.repository.UserRepository;
import com.scb.supplychainbrief.common.util.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- NOUVEL IMPORT

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto.UserResponse createUser(UserDto.UserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use: " + request.getEmail());
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserDto.UserResponse updateUserRole(Long id, Role role) {

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

    // --- IMPLÉMENTATION JWT & UserDetailsService (CORRECTION) ---

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        // Cette méthode est requise par Spring Security et remplace l'ancienne findByEmail pour la sécurité.
        // Elle doit retourner l'entité User (qui implémente UserDetails).
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void updateRefreshToken(User user, String refreshToken) {
        // Logique pour stocker ou révoquer le refresh token
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}