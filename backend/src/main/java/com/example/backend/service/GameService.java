package com.example.backend.service;


import com.example.backend.dto.GameResponse;
import com.example.backend.exception.GameNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.GameMapper;
import com.example.backend.model.Card;
import com.example.backend.model.Game;
import com.example.backend.model.GameStatus;
import com.example.backend.model.User;
import com.example.backend.repository.GameRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;

    public GameResponse startNewGame(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Card> deck = generateShuffledDeck();

        List<Card> playerCards = new ArrayList<>(List.of(deck.removeFirst(),deck.removeFirst()));
        List<Card> dealerCards = new ArrayList<>(List.of(deck.removeFirst(),deck.removeFirst()));


        int playerScore = calculateScore(playerCards);

        Game game = Game.builder()
                .player(user)
                .playerCards(playerCards)
                .dealerCards(List.of(dealerCards.getFirst())) // вторая карта скрыта
                .playerScore(playerScore)
                .dealerScore(0)
                .status(GameStatus.PLAYER_TURN)
                .build();

        gameRepository.save(game);

        return gameMapper.toDto(game);
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

    @Transactional
    public GameResponse hit(Long gameId, String email){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        if(!game.getPlayer().getEmail().equals(email)) {
            throw new AccessDeniedException("You are not allowed to hit this game");
        }

        if(game.getStatus() != GameStatus.PLAYER_TURN) {
            throw new IllegalStateException("You can't hit after your turn is over");
        }

        List<Card> deck = generateShuffledDeck();
        Card newCard = deck.removeFirst();
        game.getPlayerCards().add(newCard);

        int newScore = calculateScore(game.getPlayerCards());
        game.setPlayerScore(newScore);

        if(newScore > 21){
            game.setStatus(GameStatus.DEALER_WON);
        }

        return gameMapper.toDto(gameRepository.save(game));
    }

    @Transactional
    public GameResponse stand (Long gameId, String email){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        if(!game.getPlayer().getEmail().equals(email)) {
            throw new AccessDeniedException("You can't hit this game");
        }

        if (game.getStatus() != GameStatus.PLAYER_TURN) {
            throw new IllegalStateException("You can't stand after your turn is over");
        }

        List<Card> dealerCards = new ArrayList<>(game.getDealerCards());

        List<Card> deck = generateShuffledDeck();
        dealerCards.remove(deck.removeFirst());

        int dealerScore = calculateScore(dealerCards);

        while(dealerScore > 17){
            Card newCard = deck.removeFirst();
            dealerCards.add(newCard);
            dealerScore = calculateScore(dealerCards);
        }

        int playerScore = game.getPlayerScore();
        GameStatus result;

        if (dealerScore > 21 || playerScore > dealerScore) {
            result = GameStatus.PLAYER_WON;
        } else if (dealerScore == playerScore) {
            result = GameStatus.DRAW;
        } else {
            result = GameStatus.DEALER_WON;
        }

        game.setDealerCards(dealerCards);
        game.setDealerScore(dealerScore);
        game.setStatus(result);

        return gameMapper.toDto(gameRepository.save(game));
    }


}
