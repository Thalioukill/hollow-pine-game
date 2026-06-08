package game.strategy;

import model.cards.Animal;
import model.cards.Obstacle;
import model.stock.AnimalStock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SecondGame implements GameStrategy {
    // méthode qui stocke juste les cartes à mettre dans le deck de l'players.Opponent !
    public ArrayList<Animal> getOpponentDeck(){
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> opponentDeck = new ArrayList<>();
        opponentDeck.add(stock.getCard(AnimalStock.LOUVETEAU).get());
        opponentDeck.add(stock.getCard(AnimalStock.COYOTE).get());
        opponentDeck.add(stock.getCard(AnimalStock.CORBEAU).get());
        opponentDeck.add(stock.getCard(AnimalStock.LOUP).get());
        opponentDeck.add(stock.getCard(AnimalStock.PUNAISE).get());
        opponentDeck.add(stock.getCard(AnimalStock.ELAN).get());
        opponentDeck.add(stock.getCard(AnimalStock.HERMINE).get());

        return opponentDeck;
    }

    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesOpponent() {
        return new HashMap<>();
    }
    // méthode qui stocke les obstacles de ce tour...
    public HashMap<Integer, Optional<Obstacle>> initObstaclesUser() {
        return new HashMap<>();
    }

    @Override
    public ArrayList<Integer> getPrevisionAction(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> new ArrayList<>(List.of(0));
            case 2 -> new ArrayList<>(List.of(1));
            case 3 -> new ArrayList<>(List.of(3));
            case 4 -> new ArrayList<>(List.of(0));
            case 5 -> new ArrayList<>(List.of(2));
            default -> new ArrayList<>();
        };
    }

    @Override
    public ArrayList<Integer> getInitialPrevision() {
        return new ArrayList<>(List.of(2));
    }
}
