package com.example.namber2.service;

import com.example.namber2.dto.UserDto;
import com.example.namber2.entity.UserEntity;
import com.example.namber2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto.Response createUser(UserDto.CreateRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Пользователь с email " + request.getEmail() + " уже существует");
        }

        UserEntity newEntity = mapToEntity(request);
        newEntity.setEmail(email);

        UserEntity savedEntity = userRepository.save(newEntity);
        return mapToResponseDto(savedEntity);
    }

    public List<UserDto.Response> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public UserDto.Response getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponseDto)
                .orElseThrow(() -> new RuntimeException("Сотрудник с ID " + id + " не найден"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Сотрудник с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    public UserDto.Response updateUser(Long id, UserDto.UpdateRequest request) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сотрудник с ID " + id + " не найден"));

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!newEmail.equals(existingUser.getEmail()) &&
                    userRepository.existsByEmail(newEmail)) {
                throw new RuntimeException("Пользователь с email " + request.getEmail() + " уже существует");
            }
        }

        updateEntityFromRequest(existingUser, request);
        UserEntity updatedUser = userRepository.save(existingUser);
        return mapToResponseDto(updatedUser);
    }

    private UserEntity mapToEntity(UserDto.CreateRequest request) {
        return UserEntity.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }

    private UserDto.Response mapToResponseDto(UserEntity entity) {
        return new UserDto.Response(entity);
    }

    private void updateEntityFromRequest(UserEntity entity, UserDto.UpdateRequest request) {
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            entity.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            entity.setLastName(request.getLastName().trim());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            entity.setEmail(request.getEmail().trim().toLowerCase());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            entity.setPassword(request.getPassword());
        }
        if (request.getRole() != null) {
            entity.setRole(request.getRole());
        }
    }
}