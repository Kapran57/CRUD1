package com.example.namber2.service;

import com.example.namber2.dto.CreateRequest;
import com.example.namber2.dto.Response;
import com.example.namber2.dto.UpdateRequest;
import com.example.namber2.entity.UserEntity;
import com.example.namber2.exception.DuplicateResourceException;
import com.example.namber2.exception.ResourceNotFoundException;
import com.example.namber2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Response createUser(CreateRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Пользователь", "email", request.getEmail());
        }

        UserEntity newEntity = mapToEntity(request);
        newEntity.setEmail(email);

        UserEntity savedEntity = userRepository.save(newEntity);
        return mapToResponseDto(savedEntity);
    }

    public List<Response> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public Response getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник", "ID", id));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Сотрудник", "ID", id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public Response updateUser(Long id, UpdateRequest request) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник", "ID", id));

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!newEmail.equals(existingUser.getEmail()) &&
                    userRepository.existsByEmail(newEmail)) {
                throw new DuplicateResourceException("Пользователь", "email", request.getEmail());
            }
        }

        updateEntityFromRequest(existingUser, request);
        UserEntity updatedUser = userRepository.save(existingUser);
        return mapToResponseDto(updatedUser);
    }

    private UserEntity mapToEntity(CreateRequest request) {
        return UserEntity.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }

    private Response mapToResponseDto(UserEntity entity) {
        return new Response(entity);
    }

    private void updateEntityFromRequest(UserEntity entity, UpdateRequest request) {
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            entity.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            entity.setLastName(request.getLastName().trim());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            entity.setEmail(request.getEmail().trim().toLowerCase());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            entity.setPassword(request.getPassword());
        }
        if (request.getRole() != null) {
            entity.setRole(request.getRole());
        }
    }
}