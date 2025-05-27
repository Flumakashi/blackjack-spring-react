package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    private int playerScore;
    private int dealerScore;

    @ElementCollection
    @CollectionTable(name = "player_cards", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "card")
    private List<String> playerCards;

    @ElementCollection
    @CollectionTable(name = "dealer_cards", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "card")
    private List<String> dealerCards;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
