package com.example.namber2.dto;

import com.example.namber2.entity.UserEntity;
import com.example.namber2.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
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