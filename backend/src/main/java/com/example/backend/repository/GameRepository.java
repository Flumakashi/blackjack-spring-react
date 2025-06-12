package com.example.backend.repository;

import com.example.backend.model.Game;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayer(User player);

}
