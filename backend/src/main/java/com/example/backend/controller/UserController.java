package com.example.backend.controller;


import com.example.backend.dto.UserResponse;
import com.example.backend.dto.UserUpdateRequest;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestHeader("Authorization") String authHeader,
                                                   @RequestBody UserUpdateRequest request) {
        String token = extractToken(authHeader);
        String email = jwtUtils.extractEmail(token);
        UserResponse updated = userService.updateUserProfile(email, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        String email = jwtUtils.extractEmail(token);
        UserResponse user = userService.getCurrentUser(email);
        return ResponseEntity.ok(user);
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Invalid Authorization header");
    }

}
