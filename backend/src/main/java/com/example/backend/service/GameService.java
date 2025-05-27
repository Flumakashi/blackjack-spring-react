package com.example.backend.service;


import com.example.backend.model.Game;
import com.example.backend.model.GameStatus;
import com.example.backend.model.User;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public Game startNewGame(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> deck = generateShuffledDeck();

        List<String> playerCards = new ArrayList<>();
        playerCards.add(deck.remove(0));
        playerCards.add(deck.remove(0));

        List<String> dealerCards = new ArrayList<>();
        dealerCards.add(deck.remove(0));
        dealerCards.add(deck.remove(0));

        int playerScore = calculateScore(playerCards);
        int dealerScore = calculateScore(dealerCards);

        Game game = Game.builder()
                .player(user)
                .playerCards(playerCards)
                .dealerCards(List.of(dealerCards.get(0))) // скрываем 1 карту
                .playerScore(playerScore)
                .dealerScore(0)
                .status(GameStatus.PLAYER_TURN)
                .createdAt(LocalDateTime.now())
                .build();

        return gameRepository.save(game);
    }

    // TODO: методы hit(), stand(), getGameById()

    private List<String> generateShuffledDeck() {
        List<String> deck = new ArrayList<>();
        String[] suits = {"♠", "♥", "♦", "♣"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for(String suit : suits) {
            for(String rank : ranks) {
                deck.add(rank + suit);
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private int calculateScore(List<String> cards) {
        int score = 0;
        int aceCount = 0;
        for (String card : cards) {
            String rank = card.replaceAll("[^0-9JQKA]", "");

            if("JQK".contains(rank)) {
                score += 10;
            } else if ("A".equals(rank)) {
                aceCount++;
                score += 11;
            } else {
                score += Integer.parseInt(rank);
            }
        }

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

}
