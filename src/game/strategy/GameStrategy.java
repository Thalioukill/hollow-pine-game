package game.strategy;

import model.cards.Animal;
import model.cards.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public interface GameStrategy {
    HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent();

    HashMap<Integer, Optional<Obstacle>> initObstaclesUser();

    ArrayList<Animal> getOpponentDeck();

    ArrayList<Integer> getPrevisionAction(int roundNumber);

    // pour initialiser le previsionLine dans le game.GameManager avant de faire les rounds...
    ArrayList<Integer> getInitialPrevision();

}


