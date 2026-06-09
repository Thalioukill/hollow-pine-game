import model.board.BoardLine;
import model.cards.Animal;
import model.cards.AnimalType;
import model.players.User;
import model.stock.AnimalStock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardLineTest {

    private Animal hermine() { return new AnimalStock().getCard(AnimalType.HERMINE).get(); }
    private Animal loup() { return new AnimalStock().getCard(AnimalType.LOUP).get(); }
    private Animal punaise() { return new AnimalStock().getCard(AnimalType.PUNAISE).get(); }
    private Animal louveteau() { return new AnimalStock().getCard(AnimalType.LOUVETEAU).get(); }

    private User userWith(Animal... animals) {
        ArrayList<Animal> deck = new ArrayList<>();
        for (Animal a : animals){
            deck.add(a);
        }
        return new User(deck, new BoardLine());
    }

    @Test
    public void board_initiallyEmpty() {
        User user = userWith();
        for (int i = 0; i < 4; i++) {
            assertFalse(user.getCardAt(i).isPresent(),"Tous les emplacements du plateau devraient être vides à l'initialisation");
        }
    }

    @Test
    public void board_placeCardOccupiesSlot() {
        User user = userWith();
        user.addCard(hermine());
        user.playCardToBoard(0, 2);

        assertTrue(user.getCardAt(2).isPresent(),"L'emplacement 2 devrait être occupé après playCardToBoard");
    }

    @Test
    public void board_slotRemainsEmptyIfNotUsed() {
        User user = userWith();
        user.addCard(hermine());
        user.playCardToBoard(0, 0);

        assertFalse(user.getCardAt(1).isPresent(),"L'emplacement 1 devrait rester vide");
        assertFalse(user.getCardAt(2).isPresent(),"L'emplacement 2 devrait rester vide");
        assertFalse(user.getCardAt(3).isPresent(),"L'emplacement 3 devrait rester vide");
    }

    @Test
    public void board_cardNamePreservedOnBoard() {
        User user = userWith();
        user.addCard(loup());
        user.playCardToBoard(0, 3);

        assertEquals("Loup", user.getCardAt(3).get().getName(),"Le nom de la carte sur le plateau devrait être 'Loup'");
    }

    @Test
    public void board_sacrificeEmptiesSlot() {
        User user = userWith();
        user.addCard(hermine());
        user.playCardToBoard(0, 1);

        user.sacrifice(1);

        assertFalse(user.getCardAt(1).isPresent(),"L'emplacement 1 devrait être vide après le sacrifice");
    }

    @Test
    public void board_sacrificeGivesOneBone() {
        User user = userWith();
        user.addCard(hermine());
        user.playCardToBoard(0, 0);

        user.sacrifice(0);

        assertEquals(1, user.getBonesToken(),"Sacrifier une carte devrait donner 1 os");
    }

    @Test
    public void board_multipleSacrificesAccumulateBones() {
        User user = userWith();
        user.addCard(hermine());
        user.addCard(hermine());
        user.addCard(hermine());
        user.playCardToBoard(0, 0);
        user.playCardToBoard(0, 1);
        user.playCardToBoard(0, 2);

        user.sacrifice(0);
        user.sacrifice(1);
        user.sacrifice(2);

        assertEquals(3, user.getBonesToken(),"3 sacrifices devraient donner 3 os");
    }

    @Test
    public void board_spendBonesReducesCount() {
        User user = userWith();
        user.addCard(hermine());
        user.addCard(hermine());
        user.playCardToBoard(0, 0);
        user.playCardToBoard(0, 1);
        user.sacrifice(0);
        user.sacrifice(1); // 2 os

        user.spendBones(2);

        assertEquals(0, user.getBonesToken(),"spendBones(2) depuis 2 os devrait laisser 0 os");
    }

    @Test
    public void board_hasEnoughBonesTrue() {
        User user = userWith();
        user.addCard(hermine());
        user.playCardToBoard(0, 0);
        user.sacrifice(0); // 1 os

        // La punaise coûte 2 os
        assertFalse(user.hasEnoughBones(punaise()),"1 os n'est pas suffisant pour une carte qui en coûte 2");
    }

    @Test
    public void board_hasEnoughBonesFalse() {
        User user = userWith();
        for (int i = 0; i < 2; i++) {
            user.addCard(hermine());
            user.playCardToBoard(0, i);
            user.sacrifice(i);
        } // 2 os

        assertTrue(user.hasEnoughBones(punaise()),"2 os suffisent pour une carte qui en coûte 2");
    }

    @Test
    public void board_notifyTurnStartedTriggersGrowth() {
        User user = userWith();
        user.addCard(louveteau());
        user.playCardToBoard(0, 0);

        user.notifyTurnStarted(); // tour 1
        assertEquals("Louveteau", user.getCardAt(0).get().getName(),"Pas de transformation au premier tour");

        user.notifyTurnStarted(); // tour 2
        assertEquals("Loup", user.getCardAt(0).get().getName(),"Le Louveteau devrait se transformer en Loup au second tour");
    }

    @Test
    public void board_getSacrificableCountOnlyCountsBoard() {
        User user = userWith();
        user.addCard(hermine());
        user.addCard(hermine());
        // 2 cartes en main, 0 sur le board

        assertEquals(0, user.getSacrificableCount(),"Les cartes en main ne comptent pas comme sacrifiable");

        user.playCardToBoard(0, 0);
        assertEquals(1, user.getSacrificableCount(),"1 carte sur le board = 1 sacrifiable");

        user.playCardToBoard(0, 1);
        assertEquals(2, user.getSacrificableCount(),"2 cartes sur le board = 2 sacrifiables");
    }

    @Test
    public void board_getBoardLineCardAtEmpty() {
        User user = userWith();
        assertFalse(user.getBoardLineCardAt(0).isPresent(),"getBoardLineCardAt sur un slot vide devrait retourner Optional.empty()");
    }

    @Test
    public void board_getBoardLineCardAtPresent() {
        User user = userWith();
        user.addCard(loup());
        user.playCardToBoard(0, 2);

        assertTrue(user.getBoardLineCardAt(2).isPresent(),"getBoardLineCardAt(2) devrait retourner le Loup");
        assertEquals("Loup", user.getBoardLineCardAt(2).get().getName());
    }
}
