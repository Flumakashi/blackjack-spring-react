package com.example.backend.service;


import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.Card;
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
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Card> deck = generateShuffledDeck();

        List<Card> playerCards = new ArrayList<>(List.of(deck.removeFirst(),deck.removeFirst()));
        List<Card> dealerCards = new ArrayList<>(List.of(deck.removeFirst(),deck.removeFirst()));


        int playerScore = calculateScore(playerCards);
//        int dealerScoreHidden = calculateScore(List.of(dealerCards.getFirst()));

        Game game = Game.builder()
                .player(user)
                .playerCards(playerCards)
                .dealerCards(List.of(dealerCards.getFirst())) // вторая карта скрыта
                .playerScore(playerScore)
                .dealerScore(0)
                .status(GameStatus.PLAYER_TURN)
                .build();

        return gameRepository.save(game);
    }

    // TODO: методы hit(), stand(), getGameById()

    private List<Card> generateShuffledDeck() {
        List<Card> deck = new ArrayList<>();
        String[] suits = {"♠", "♥", "♦", "♣"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for(String suit : suits) {
            for(String rank : ranks) {
                deck.add(Card.builder()
                        .rank(rank)
                        .suit(suit)
                        .build());
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private int calculateScore(List<Card> cards) {
        int score = 0;
        int aceCount = 0;
        for (Card card : cards) {
            String rank = card.getRank();

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
