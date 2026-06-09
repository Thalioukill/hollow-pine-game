import game.strategy.FirstGame;
import model.abilities.AbilityType;
import model.board.BoardLine;
import model.cards.Animal;
import model.cards.AnimalType;
import model.cards.Obstacle;
import model.players.User;
import model.stock.AnimalStock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameManagerTest {

    // méthode @Before est executé avant

    private Animal vipere() { return new AnimalStock().getCard(AnimalType.VIPERE).get(); }
    private Animal hermine() { return new AnimalStock().getCard(AnimalType.HERMINE).get(); }
    private Animal loup() { return new AnimalStock().getCard(AnimalType.LOUP).get(); }

    // Pierre de sacrifice

    @Test
    public void sacrifice_transferAbility_addsToReceiver() {
        Animal from = vipere(); // Contact Mortel
        Animal to = loup();

        ArrayList<Animal> deck = new ArrayList<>();
        deck.add(from);
        deck.add(to);
        User user = new User(deck, new BoardLine());

        model.abilities.Ability ability = from.getTransferableAbility().get();
        user.transferAbility(from, ability, to);

        assertTrue(to.hasAnAbility(AbilityType.DEATH_TOUCH),"Le Loup devrait avoir Contact Mortel après le transfert");
    }

    @Test
    public void sacrifice_transferAbility_removesSourceFromDeckSave() {
        Animal from = vipere();
        Animal to = loup();

        ArrayList<Animal> deck = new ArrayList<>();
        deck.add(from);
        deck.add(to);
        User user = new User(deck, new BoardLine());

        model.abilities.Ability ability = from.getTransferableAbility().get();
        user.transferAbility(from, ability, to);

        assertFalse(user.getDeckSave().stream().anyMatch(a -> a.getName().equals("Vipère")),"La Vipère devrait être retirée du deckSave après le sacrifice");
    }

    @Test
    public void sacrifice_transferAbility_throwsIfAlreadyHas() {
        Animal from = vipere();
        Animal to = vipere(); // possède déjà Contact Mortel

        ArrayList<Animal> deck = new ArrayList<>();
        deck.add(from);
        deck.add(to);
        User user = new User(deck, new BoardLine());

        model.abilities.Ability ability = from.getTransferableAbility().get();

        assertThrows(IllegalArgumentException.class,() -> user.transferAbility(from, ability, to),"Transférer une ability déjà possédée devrait lever une exception");
    }

    @Test
    public void sacrifice_pool_onlyContainsCardsWithAbility() {
        ArrayList<Animal> deck = new ArrayList<>();
        deck.add(hermine()); // pas d'ability
        deck.add(vipere()); // Contact Mortel
        deck.add(loup());  // pas d'ability
        User user = new User(deck, new BoardLine());

        ArrayList<Animal> pool = user.getSacrificePool();

        assertEquals(1, pool.size(),"Le pool de sacrifice devrait contenir uniquement les cartes avec une ability");
        assertEquals("Vipère", pool.get(0).getName());
    }

    @Test
    public void setup_obstaclesPlacedCorrectly() {
        FirstGame strategy = new FirstGame();
        BoardLine line = new BoardLine();

        HashMap<Integer, Optional<Obstacle>> obstacles = strategy.initObstaclesOpponent();
        for (int i = 0; i < obstacles.size(); i++) {
            Optional<Obstacle> opt = obstacles.get(i);
            if (opt.isPresent()){
                line.placeCardAt(opt.get(), i);
            }
        }

        assertTrue(line.getCardAt(0).isPresent(), "FirstGame devrait placer un obstacle en position 0 pour l'adversaire");
        assertEquals("Sapin", line.getCardAt(0).get().getName(), "L'obstacle en position 0 devrait être un Sapin");
        assertFalse(line.getCardAt(0).get().isSacrificable(), "Un obstacle ne devrait pas être sacrifiable");
    }
}
