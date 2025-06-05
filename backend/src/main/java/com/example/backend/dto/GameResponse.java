package com.example.backend.dto;


import com.example.backend.model.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {
    private Long id;
    private List<CardDto> playedCards;
    private List<CardDto> dealerCards;
    private int playerScore;
    private int dealerScore;
    private GameStatus gameStatus;
}
