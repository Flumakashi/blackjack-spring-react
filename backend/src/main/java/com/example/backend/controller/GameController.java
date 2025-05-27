package com.example.backend.controller;


import com.example.backend.model.Game;
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
    public ResponseEntity<Game> startGame(@RequestHeader("Authorization") String token) {
        String email = jwtUtils.extractEmail(token.replace("Bearer", ""));
        Game game = gameService.startNewGame(email);
        return ResponseEntity.ok(game);
    }

    // TODO: методы /hit, /stand, /status/{id}
}
