package com.example.namber2.dto;

import com.example.namber2.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private String firstName;
    private String lastName;

    @Email(message = "Некорректный формат email")
    private String email;

    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    private Role role;
}