package com.example.backend.controller;


import com.example.backend.dto.GameResponse;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final JwtUtils jwtUtils;

    @PostMapping("/start")
    public ResponseEntity<GameResponse> startGame(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        String email = jwtUtils.extractEmail(token);
        GameResponse game = gameService.startNewGame(email);
        return ResponseEntity.ok(game);
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Invalid Authorization header");
    }


    // TODO: методы /hit, /stand, /status/{id}
}
