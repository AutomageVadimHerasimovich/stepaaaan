package com.github.vadimher.library.mapper;

import com.github.vadimher.library.dto.UserDto;
import com.github.vadimher.library.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public UserEntity userDtoToUser(UserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.id());
        entity.setUsername(dto.username());
        entity.setPassword(dto.password());
        entity.setRole(dto.role());
        return entity;
    }
}

