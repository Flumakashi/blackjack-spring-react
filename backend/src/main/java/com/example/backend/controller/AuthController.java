package com.example.backend.controller;


import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.UserRegisterRequest;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

   @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
       userService.registerUser(request);
       return ResponseEntity.ok("User registered successfully");
   }

   @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
       );

       UserDetails user = (UserDetails) authentication.getPrincipal();
       String token = jwtUtils.generateToken(user.getUsername());

       return ResponseEntity.ok(new AuthResponse(token));
   }


}
