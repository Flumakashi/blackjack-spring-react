package com.example.backend.mapper;

import com.example.backend.dto.CardDto;
import com.example.backend.dto.GameResponse;
import com.example.backend.model.Card;
import com.example.backend.model.Game;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameMapper {

    public GameResponse toDto(Game game) {
        return new GameResponse(
                game.getId(),
                mapCards(game.getPlayerCards()),
                mapCards(game.getDealerCards()),
                game.getPlayerScore(),
                game.getDealerScore(),
                game.getStatus()
        );
    }

    public List<CardDto> mapCards(List<Card> cards) {
        return cards.stream()
                .map(card -> new CardDto(card.getRank(), card.getSuit()))
                .collect(Collectors.toList());
    }
}
