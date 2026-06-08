import game.strategy.FirstGame;
import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Obstacle;
import model.players.User;
import model.stock.AnimalStock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Animal hermine()   { return new AnimalStock().getCard(AnimalStock.HERMINE).get(); }
    private Animal loup()      { return new AnimalStock().getCard(AnimalStock.LOUP).get(); }


    private User userWith(Animal... animals) {
        ArrayList<Animal> deck = new ArrayList<>();
        for (Animal a : animals){
            deck.add(a);
        }
        return new User(deck, new BoardLine());
    }

    // Victoire / Défaite

    @Test
    public void winCondition_scoreReaches5() {
        BoardLine attackers = new BoardLine();
        BoardLine defenders = new BoardLine();

        attackers.placeCardAt(loup(), 0); // 3
        attackers.placeCardAt(hermine(), 1); // 1
        attackers.placeCardAt(hermine(), 2); // 1

        int score = 0;
        score += attackers.attack(defenders);

        assertTrue(score >= 5,"5 dégâts directs devraient déclencher la victoire");
    }

    @Test
    public void lossCondition_opponentScoreReaches5() {
        BoardLine opponentLine = new BoardLine();
        BoardLine userLine = new BoardLine();

        opponentLine.placeCardAt(loup(), 0);// 3
        opponentLine.placeCardAt(hermine(), 1); // 1
        opponentLine.placeCardAt(hermine(), 2); // 1

        int score = 0;
        score -= opponentLine.attack(userLine);

        assertTrue(Math.abs(score) >= 5, "5 dégâts directs de l'adversaire devraient déclencher la défaite");
    }

    @Test
    public void winCondition_notTriggeredBelow5() {
        BoardLine attackers = new BoardLine();
        BoardLine defenders = new BoardLine();

        attackers.placeCardAt(hermine(), 0); // 1 ATK
        attackers.placeCardAt(hermine(), 1); // 1 ATK

        int score = attackers.attack(defenders); // 2 dégâts

        assertFalse(Math.abs(score) >= 5,"2 dégâts ne devraient pas déclencher la fin de partie");
    }

    @Test
    public void winCondition_exactly5Triggers() {
        BoardLine attackers = new BoardLine();
        BoardLine defenders = new BoardLine();

        attackers.placeCardAt(loup(), 0);    // 3
        attackers.placeCardAt(hermine(), 1); // 1
        attackers.placeCardAt(hermine(), 2); // 1

        int score = attackers.attack(defenders); // exactement 5

        assertTrue(Math.abs(score) >= 5,"Un score de exactement 5 devrait déclencher la fin de partie");
    }
}
