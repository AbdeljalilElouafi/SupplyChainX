package com.scb.supplychainbrief.common.dto;

import com.scb.supplychainbrief.common.util.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class UserDto {

    @Data
    public static class UserRequest {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        @NotNull
        private Role role;
    }

    @Data
    public static class UserResponse {
        private Long idUser;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
    }

    @Data
    public static class RoleUpdateRequest {
        @NotNull
        private Role role;
    }
}
