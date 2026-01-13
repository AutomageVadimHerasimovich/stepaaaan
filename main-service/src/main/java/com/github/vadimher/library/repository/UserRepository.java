package com.github.vadimher.library.repository;

import com.github.vadimher.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    void deleteByUsername(String username);
}