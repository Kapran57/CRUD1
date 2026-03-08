package com.example.namber2.dto;

import com.example.namber2.entity.UserEntity;
import com.example.namber2.enum1.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {

        @NotBlank(message = "Имя обязательно")
        private String firstName;

        @NotBlank(message = "Фамилия обязательна")
        private String lastName;

        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        private String email;

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
        private String password;

        @NotNull(message = "Роль обязательна")
        private Role role;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {

        private String firstName;

        private String lastName;

        @Email(message = "Некорректный формат email")
        private String email;

        @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
        private String password;

        private Role role;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;

        public Response(UserEntity user) {
            this.id = user.getId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.email = user.getEmail();
            this.role = user.getRole();
        }
    }
}
