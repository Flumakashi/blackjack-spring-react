package com.example.backend.controller;


import com.example.backend.dto.UserRegisterRequest;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

   @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
       userService.registerUser(request);
       return ResponseEntity.ok("User registered successfully");
   }


}
