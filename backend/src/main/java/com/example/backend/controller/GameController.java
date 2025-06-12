package com.example.backend.controller;


import com.example.backend.dto.GameResponse;
import com.example.backend.mapper.GameMapper;
import com.example.backend.model.Game;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final JwtUtils jwtUtils;
    private final GameMapper gameMapper;

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

    @PostMapping("/{id}/hit")
    public ResponseEntity<GameResponse> hit(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        String email = jwtUtils.extractEmail(token);

        GameResponse updatedGame = gameService.hit(id, email);
        return ResponseEntity.ok(updatedGame);

    }

    @PostMapping("/{id}/stand")
    public ResponseEntity<GameResponse> stand(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader){

        String token = extractToken(authHeader);
        String email = jwtUtils.extractEmail(token);

        GameResponse response = gameService.stand(id, email);
        return ResponseEntity.ok(response);
    }

    // TODO: методы /status/{id}
}
