package test;

import com.scb.supplychainbrief.common.dto.UserDto;
import com.scb.supplychainbrief.common.mapper.UserMapper;
import com.scb.supplychainbrief.common.model.User;
import com.scb.supplychainbrief.common.repository.UserRepository;
import com.scb.supplychainbrief.common.service.UserServiceImpl;
import com.scb.supplychainbrief.common.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUser_Successfully() {


        UserDto.UserRequest request = new UserDto.UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setRole(Role.ADMIN);


        User userToSave = new User();
        userToSave.setEmail("test@example.com");
        userToSave.setRole(Role.ADMIN);


        User savedUser = new User();
        savedUser.setIdUser(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setRole(Role.ADMIN);
        savedUser.setPassword("hashed_password_abc123");


        UserDto.UserResponse responseDto = new UserDto.UserResponse();
        responseDto.setIdUser(1L);
        responseDto.setEmail("test@example.com");
        responseDto.setRole(Role.ADMIN);


        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        when(userMapper.toUser(request)).thenReturn(userToSave);

        when(passwordEncoder.encode("password123")).thenReturn("hashed_password_abc123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(userMapper.toUserResponse(savedUser)).thenReturn(responseDto);


        UserDto.UserResponse result = userService.createUser(request);


        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertEquals("test@example.com", result.getEmail());


        verify(passwordEncoder, times(1)).encode("password123");
    }
}
