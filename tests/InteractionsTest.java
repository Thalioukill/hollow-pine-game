import org.junit.Test;
import org.junit.Before;
import java.util.Scanner;

import static org.junit.Assert.*;

public class InteractionsTest {

    private CardStock stock;

    @Before
    public void setUp() {
        stock = new CardStock();
    }

    // ===================== HELPERS =====================

    private User buildUserWithEmptyBoard() {
        return new User(new Deck());
    }

    private User buildUserWithCardAt(int position) {
        User human = buildUserWithEmptyBoard();
        Animal squirrel = (Animal) stock.getCard(CardStock.ECUREUIL).get();
        human.placeCardAt(squirrel, position);
        return human;
    }

    private User buildUserWithCardsInHand(int count) {
        Deck deck = new Deck();
        for (int i = 0; i < count; i++) {
            deck.addCard((Animal) stock.getCard(CardStock.ECUREUIL).get());
        }
        User human = new User(deck);
        // Simule une Interactions muette pour piocher sans affichage
        Interactions silent = new Interactions(new Scanner("OUI\nOUI\nOUI\nOUI\nOUI\n"));
        for (int i = 0; i < count; i++) {
            human.draw(silent);
        }
        return human;
    }

    // ===================== askConfirmation =====================

    @Test
    public void testAskConfirmation_OUI_returnsTrue() {
        Interactions interactions = new Interactions(new Scanner("OUI\n"));
        assertTrue(interactions.askConfirmation("Confirmer ?"));
    }

    @Test
    public void testAskConfirmation_NON_returnsFalse() {
        Interactions interactions = new Interactions(new Scanner("NON\n"));
        assertFalse(interactions.askConfirmation("Confirmer ?"));
    }

    @Test
    public void testAskConfirmation_invalidThenOUI_returnsTrue() {
        Interactions interactions = new Interactions(new Scanner("blabla\nOUI\n"));
        assertTrue(interactions.askConfirmation("Confirmer ?"));
    }

    @Test
    public void testAskConfirmation_invalidThenNON_returnsFalse() {
        Interactions interactions = new Interactions(new Scanner("xyz\nNON\n"));
        assertFalse(interactions.askConfirmation("Confirmer ?"));
    }

    // ===================== askPosition =====================

    @Test
    public void testAskPosition_validEmptySlot_returnsPosition() {
        User human = buildUserWithEmptyBoard();
        Interactions interactions = new Interactions(new Scanner("1\n"));
        assertEquals(1, interactions.askPosition(human));
    }

    @Test
    public void testAskPosition_allValidPositions_returnCorrectly() {
        for (int pos = 1; pos <= 4; pos++) {
            User human = buildUserWithEmptyBoard();
            Interactions interactions = new Interactions(new Scanner(pos + "\n"));
            assertEquals(pos, interactions.askPosition(human));
        }
    }

    @Test
    public void testAskPosition_occupiedThenFree_returnsSecond() {
        User human = buildUserWithCardAt(1);
        // 1 occupé, 2 libre
        Interactions interactions = new Interactions(new Scanner("1\n2\n"));
        assertEquals(2, interactions.askPosition(human));
    }

    @Test
    public void testAskPosition_outOfBoundsThenValid_returnsValid() {
        User human = buildUserWithEmptyBoard();
        // 9 invalide, puis 3 valide
        Interactions interactions = new Interactions(new Scanner("9\n3\n"));
        assertEquals(3, interactions.askPosition(human));
    }

    @Test
    public void testAskPosition_nonIntThenValid_returnsValid() {
        User human = buildUserWithEmptyBoard();
        Interactions interactions = new Interactions(new Scanner("abc\n2\n"));
        assertEquals(2, interactions.askPosition(human));
    }

    // ===================== askPositionSacrifice =====================

    @Test
    public void testAskPositionSacrifice_validCard_returnsCorrectSlot() {
        User human = buildUserWithCardAt(2);
        Interactions interactions = new Interactions(new Scanner("2\n"));
        SlotSelection result = interactions.askPositionSacrifice(human);
        assertEquals(2, result.getPosition());
        assertNotNull(result.getCard());
    }

    @Test
    public void testAskPositionSacrifice_emptySlotThenOccupied_returnsOccupied() {
        // position 1 vide, position 2 occupée
        User human = buildUserWithCardAt(2);
        Interactions interactions = new Interactions(new Scanner("1\n2\n"));
        SlotSelection result = interactions.askPositionSacrifice(human);
        assertEquals(2, result.getPosition());
    }

    @Test
    public void testAskPositionSacrifice_outOfBoundsThenValid_returnsValid() {
        User human = buildUserWithCardAt(3);
        Interactions interactions = new Interactions(new Scanner("9\n3\n"));
        SlotSelection result = interactions.askPositionSacrifice(human);
        assertEquals(3, result.getPosition());
    }

    @Test
    public void testAskPositionSacrifice_nonIntThenValid_returnsValid() {
        User human = buildUserWithCardAt(1);
        Interactions interactions = new Interactions(new Scanner("abc\n1\n"));
        SlotSelection result = interactions.askPositionSacrifice(human);
        assertEquals(1, result.getPosition());
    }

    // ===================== askCardLetter =====================

    @Test
    public void testAskCardLetter_A_returnsIndex0() {
        User human = buildUserWithCardsInHand(3);
        Interactions interactions = new Interactions(new Scanner("A\n"));
        AnimalSelection result = interactions.askCardLetter(human.getHandSize(), human);
        assertEquals(0, result.getIndex());
        assertNotNull(result.getAnimal());
    }

    @Test
    public void testAskCardLetter_B_returnsIndex1() {
        User human = buildUserWithCardsInHand(3);
        Interactions interactions = new Interactions(new Scanner("B\n"));
        AnimalSelection result = interactions.askCardLetter(human.getHandSize(), human);
        assertEquals(1, result.getIndex());
    }

    @Test
    public void testAskCardLetter_lowercase_accepted() {
        User human = buildUserWithCardsInHand(2);
        Interactions interactions = new Interactions(new Scanner("a\n"));
        AnimalSelection result = interactions.askCardLetter(human.getHandSize(), human);
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testAskCardLetter_outOfRangeThenValid_returnsValid() {
        // Main de 2 cartes : A et B. Z invalide, puis A valide.
        User human = buildUserWithCardsInHand(2);
        Interactions interactions = new Interactions(new Scanner("Z\nA\n"));
        AnimalSelection result = interactions.askCardLetter(human.getHandSize(), human);
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testAskCardLetter_numberThenValid_returnsValid() {
        User human = buildUserWithCardsInHand(2);
        Interactions interactions = new Interactions(new Scanner("1\nB\n"));
        AnimalSelection result = interactions.askCardLetter(human.getHandSize(), human);
        assertEquals(1, result.getIndex());
    }
}