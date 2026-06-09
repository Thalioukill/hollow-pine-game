import model.board.BoardLine;
import model.cards.Animal;
import model.cards.AnimalType;
import model.players.User;
import model.stock.AnimalStock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class UserTest {

    private Animal hermine() { return new AnimalStock().getCard(AnimalType.HERMINE).get(); }
    private Animal loup() { return new AnimalStock().getCard(AnimalType.LOUP).get(); }
    private Animal elan() { return new AnimalStock().getCard(AnimalType.ELAN).get(); }
    private Animal ecureuil()  { return new AnimalStock().getCard(AnimalType.ECUREUIL).get(); }

    // les trois petits points permettent d'appeler la méthode sans creer une ArrayList à chaque fois.
    private User userWith(Animal... animals) {
        ArrayList<Animal> deck = new ArrayList<>();
        for (Animal a : animals){
            deck.add(a);
        }
        return new User(deck, new BoardLine());
    }

    // Deck

    @Test
    public void deck_initialSizeIsCorrect() {
        User user = userWith(hermine(), loup(), ecureuil());
        assertEquals(3, user.getDeckSize(),"Le deck devrait contenir 3 cartes après initialisation");
    }

    @Test
    public void deck_drawReducesSize() {
        User user = userWith(hermine(), loup());
        user.draw();
        assertEquals(1, user.getDeckSize(),"Le deck devrait avoir 1 carte après une pioche");
    }

    @Test
    public void deck_drawUntilEmpty() {
        User user = userWith(hermine(), loup(), ecureuil());
        user.draw();
        user.draw();
        user.draw();
        assertEquals(0, user.getDeckSize(),"Le deck devrait être vide après avoir tout pioché");
    }

    @Test
    public void deck_drawFromEmptyThrows() {
        User user = userWith();
        assertThrows(java.util.NoSuchElementException.class, user::draw,"Piocher d'un deck vide devrait lever une exception");
    }

    @Test
    public void deck_drawOrderIsPreserved() {
        // Les cartes doivent être piochées dans l'ordre d'insertion
        User user = userWith(hermine(), loup(), ecureuil());
        assertEquals("Hermine", user.draw().getName(),"La première carte piochée devrait être l'Hermine");
        assertEquals("Loup", user.draw().getName(),"La deuxième carte piochée devrait être le Loup");
        assertEquals("Ecureuil", user.draw().getName(),"La troisième carte piochée devrait être l'Ecureuil");
    }

    @Test
    public void deck_deepCopyIsIndependent() {
        ArrayList<Animal> original = new ArrayList<>();
        original.add(hermine());
        original.add(loup());

        ArrayList<Animal> copy = User.deepCopyDeck(original);
        original.get(0).takeDamage(3); // tue l'original

        assertEquals(3, copy.get(0).getHealthPoints(),"La copie profonde ne devrait pas être affectée par les dégâts sur l'original");
        assertFalse(copy.get(0).isDead(),"La copie de l'Hermine ne devrait pas être morte");
    }

    @Test
    public void deck_deepCopySameSize() {
        ArrayList<Animal> original = new ArrayList<>();
        for (int i = 0; i < 5; i++) original.add(hermine());

        ArrayList<Animal> copy = User.deepCopyDeck(original);
        assertEquals(5, copy.size(),"La copie devrait avoir le même nombre de cartes que l'original");
    }

    // Main
    @Test
    public void hand_initiallyEmpty() {
        User user = userWith(hermine(), loup());
        assertEquals(0, user.getHandSize(),"La main devrait être vide à l'initialisation (avant pioche)");
    }

    @Test
    public void hand_addCardIncreasesSize() {
        User user = userWith();
        user.addCard(hermine());
        assertEquals(1, user.getHandSize(),"La main devrait avoir 1 carte après addCard");
    }

    @Test
    public void hand_addMultipleCards() {
        User user = userWith();
        user.addCard(hermine());
        user.addCard(loup());
        user.addCard(ecureuil());
        assertEquals(3, user.getHandSize(),"La main devrait avoir 3 cartes");
    }

    @Test
    public void hand_getCardReturnsCorrect() {
        User user = userWith();
        user.addCard(hermine());
        user.addCard(loup());

        assertEquals("Hermine", user.getHandCard(0).getName(),"La carte en index 0 devrait être l'Hermine");
        assertEquals("Loup", user.getHandCard(1).getName(),"La carte en index 1 devrait être le Loup");
    }

    @Test
    public void hand_playCardRemovesFromHand() {
        User user = userWith();
        user.addCard(hermine());
        user.addCard(loup());

        user.playCardToBoard(0, 0); // joue Hermine
        assertEquals(1, user.getHandSize(),"La main devrait avoir 1 carte après avoir joué l'Hermine");
        assertEquals("Loup", user.getHandCard(0).getName(),"Le Loup devrait être la seule carte restante en main");
    }

    @Test
    public void hand_drawThenAddToHand() {
        User user = userWith(hermine(), loup());
        Animal drawn = user.draw();
        user.addCard(drawn);

        assertEquals(1, user.getHandSize());
        assertEquals(1, user.getDeckSize(),"Le deck devrait avoir 1 carte restante");
    }

    @Test
    public void hand_indexOutOfBoundsThrows() {
        User user = userWith();
        user.addCard(hermine());

        assertThrows(IndexOutOfBoundsException.class, () -> user.getHandCard(5),"Accéder à un index inexistant en main devrait lever une exception");
    }


    // mise en place d'une partie

    @Test
    public void setup_resetGives4CardsInHand() {
        ArrayList<Animal> deck = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            deck.add(hermine());
        }
        User user = new User(deck, new BoardLine());

        ArrayList<Animal> freshDeck = User.deepCopyDeck(deck);
        user.resetForNewGame(freshDeck);

        assertEquals(4, user.getHandSize(),"Après resetForNewGame la main devrait avoir exactement 4 cartes");
    }

    @Test
    public void setup_resetClearsBoardLine() {
        User user = userWith(hermine(), loup());
        user.addCard(hermine());
        user.playCardToBoard(0, 0); // place une carte

        ArrayList<Animal> freshDeck = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            freshDeck.add(hermine());
        }
        user.resetForNewGame(freshDeck);

        for (int i = 0; i < 4; i++) {
            assertFalse(user.getCardAt(i).isPresent(),"Le plateau devrait être vide après resetForNewGame");
        }
    }

    @Test
    public void setup_resetClearsBonesToken() {
        User user = userWith(hermine());
        user.addCard(hermine());
        user.playCardToBoard(0, 0);
        user.sacrifice(0); // donne 1 os

        assertEquals(1, user.getBonesToken());

        ArrayList<Animal> freshDeck = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            freshDeck.add(hermine());
        }
        user.resetForNewGame(freshDeck);

        assertEquals(0, user.getBonesToken(),"Les os devraient être remis à 0 après resetForNewGame");
    }

    @Test
    public void setup_deepCopyIsIndependent() {
        ArrayList<Animal> original = new ArrayList<>();
        original.add(hermine());
        original.add(loup());

        ArrayList<Animal> copy = User.deepCopyDeck(original);

        original.get(0).takeDamage(2);

        assertEquals(3, copy.get(0).getHealthPoints(),"La copie profonde ne devrait pas être affectée par les modifications de l'original");
    }


    // Récompense après la partie 2

    @Test
    public void reward_addPermanentCardIncreasesDeckSave() {
        User user = userWith(hermine(), loup());
        int before = user.getDeckSave().size();

        user.addPermanentCard(elan());

        assertEquals(before + 1, user.getDeckSave().size(),"Le deckSave devrait avoir 1 carte de plus après addPermanentCard");
    }

    @Test
    public void reward_permanentCardPersistsAcrossGames() {
        ArrayList<Animal> deck = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            deck.add(hermine());
        }
        User user = new User(deck, new BoardLine());

        user.addPermanentCard(elan());
        int savedSize = user.getDeckSave().size();

        ArrayList<Animal> freshDeck = User.deepCopyDeck(user.getDeckSave());
        user.resetForNewGame(freshDeck);

        assertEquals(savedSize, user.getDeckSave().size(),"La carte permanente devrait toujours être dans le deckSave après un reset");
    }

    @Test
    public void reward_poolExcludesOwnedCards() {
        AnimalStock stock = new AnimalStock();
        ArrayList<Animal> deck = new ArrayList<>();

        for (AnimalType type : stock.getAllAnimalTypes()) {
            stock.getCard(type).ifPresent(deck::add);
        }
        User user = new User(deck, new BoardLine());

        ArrayList<AnimalType> pool = user.getRewardPool();

        assertTrue(pool.isEmpty(),"Le pool de récompense devrait être vide si le joueur possède toutes les cartes");
    }

    @Test
    public void reward_poolContainsUnownedCards() {
        // Le joueur ne possède que des Écureuils
        ArrayList<Animal> deck = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            deck.add(ecureuil());
        }
        User user = new User(deck, new BoardLine());

        ArrayList<AnimalType> pool = user.getRewardPool();

        assertFalse(pool.isEmpty(),"Le pool de récompense devrait contenir des cartes non possédées");
        assertFalse(pool.contains(AnimalType.ECUREUIL),"L'Écureuil (déjà possédé) ne devrait pas apparaître dans le pool");
    }
}
