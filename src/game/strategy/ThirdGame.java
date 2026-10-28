package game.strategy;

import model.cards.Animal;
import model.cards.AnimalType;
import model.cards.Obstacle;
import model.cards.ObstacleType;
import model.stock.AnimalStock;
import model.stock.ObstacleStock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ThirdGame implements GameStrategy {

    // méthode qui stocke juste les cartes à mettre dans le deck de l'players.Opponent !
    public ArrayList<Animal> getOpponentDeck(){
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> opponentDeck = new ArrayList<>();
        opponentDeck.add(stock.getCard(AnimalType.MOINEAU).get());
        opponentDeck.add(stock.getCard(AnimalType.LOUP).get());
        opponentDeck.add(stock.getCard(AnimalType.VIPERE).get());
        opponentDeck.add(stock.getCard(AnimalType.PUNAISE).get());
        opponentDeck.add(stock.getCard(AnimalType.LOUVETEAU).get());
        opponentDeck.add(stock.getCard(AnimalType.ELAN).get());
        opponentDeck.add(stock.getCard(AnimalType.CORBEAU).get());

        return opponentDeck;
    }

    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent() {
        return new HashMap<>();
    }


    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser() {
        HashMap<Integer, Optional<Obstacle>> listeObstacles = new HashMap<>();
        ObstacleStock stock = new ObstacleStock();

        for (int i = 0; i < 4; i++) {
            listeObstacles.put(i, Optional.empty());
        }

        // On récupère l'Optional directement depuis le stock
        Optional<Obstacle> rocher = stock.getObstacle(ObstacleType.ROCHER);
        listeObstacles.put(2, rocher);

        return listeObstacles;
    }

    @Override
    public ArrayList<Integer> getPrevisionAction(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> new ArrayList<>(List.of(0));
            case 2 -> new ArrayList<>(List.of(3));
            case 3 -> new ArrayList<>(List.of(1));
            case 4 -> new ArrayList<>();
            case 5 -> new ArrayList<>(List.of(1));
            case 6 -> new ArrayList<>(List.of(0));
            case 7 -> new ArrayList<>(List.of(2));
            default -> new ArrayList<>();
        };
    }

    @Override
    public ArrayList<Integer> getInitialPrevision() {
        return new ArrayList<>(List.of(1)); // Moineau en position 2 derrière le rocher.
    }

}
