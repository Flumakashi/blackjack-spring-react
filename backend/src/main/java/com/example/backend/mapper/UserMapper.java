package com.example.backend.mapper;

import com.example.backend.dto.UserResponse;
import com.example.backend.dto.UserUpdateRequest;
import com.example.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public void updateFromDto(UserUpdateRequest dto, User user) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
    }
}
