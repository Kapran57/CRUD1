package com.example.namber2.repository;

import com.example.namber2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserEntity u WHERE u.email = :email AND u.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
}
