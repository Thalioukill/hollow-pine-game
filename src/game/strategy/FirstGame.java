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

public class FirstGame implements GameStrategy {

    // méthode qui stocke juste les cartes à mettre dans le deck de l'Opponent !
    public ArrayList<Animal> getOpponentDeck(){
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> opponentDeck = new ArrayList<>();
        opponentDeck.add(stock.getCard(AnimalType.HERMINE).get());
        opponentDeck.add(stock.getCard(AnimalType.LOUVETEAU).get());
        opponentDeck.add(stock.getCard(AnimalType.ELAN).get());
        opponentDeck.add(stock.getCard(AnimalType.HERMINE).get());
        opponentDeck.add(stock.getCard(AnimalType.LOUVETEAU).get());
        opponentDeck.add(stock.getCard(AnimalType.LOUP).get());
        return opponentDeck;
    }

    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent() {
        HashMap<Integer, Optional<Obstacle>> listeObstacles = new HashMap<>();
        ObstacleStock stock = new ObstacleStock();

        for (int i = 0; i < 4; i++) {
            listeObstacles.put(i, Optional.empty());
        }

        // On récupère l'Optional directement depuis le stock
        Optional<Obstacle> sapin = stock.getObstacle(ObstacleType.SAPIN);
        listeObstacles.put(0, sapin);

        return listeObstacles;
    }

    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser() {
        return new HashMap<>();
    }

    // pour remplir l'ArrayList d'un coup sans avoir à utiliser add on utiliser List.of
    @Override
    public ArrayList<Integer> getPrevisionAction(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> new ArrayList<>(List.of(0)); // après round 1 : prépare pos 0
            case 2 -> new ArrayList<>(List.of(2)); // après round 2 : prépare pos 2
            case 3 -> new ArrayList<>(List.of(2, 3)); // plusieurs cartes possibles
            default -> new ArrayList<>();
        };
    }

    @Override
    public ArrayList<Integer> getInitialPrevision() {
        return new ArrayList<>(List.of(3)); // Hermine en position 3 dès le départ
    }
}
