import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Obstacle;
import model.players.Opponent;
import model.stock.AnimalStock;
import model.stock.ObstacleStock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

import java.util.ArrayList;

public class OpponentTest {

    private Animal hermine() {
        return new AnimalStock().getCard(AnimalStock.HERMINE).get();
    }

    private Animal loup() {
        return new AnimalStock().getCard(AnimalStock.LOUP).get();
    }

    private Animal louveteau() {
        return new AnimalStock().getCard(AnimalStock.LOUVETEAU).get();
    }

    private Animal elan() {
        return new AnimalStock().getCard(AnimalStock.ELAN).get();
    }

    private Opponent opponentWith(Animal... animals) {
        ArrayList<Animal> deck = new ArrayList<>();
        for (Animal a : animals) deck.add(a);
        return new Opponent(deck, new BoardLine(), new BoardLine());
    }

    // placePrevisionCard

    @Test
    public void placePrevisionCard_placesCardAtCorrectPosition() {
        Opponent opponent = opponentWith(hermine());
        opponent.placePrevisionCard(2);

        assertTrue(opponent.getPrevisionLine().getCardAt(2).isPresent(),"La carte devrait être placée en position 2 sur la ligne de prévision");
        assertEquals("Hermine", opponent.getPrevisionLine().getCardAt(2).get().getName());
    }

    @Test
    public void placePrevisionCard_doesNotOverwriteOccupiedSlot() {
        Opponent opponent = opponentWith(hermine(), loup());

        opponent.placePrevisionCard(0);
        opponent.placePrevisionCard(0);

        assertTrue(opponent.getPrevisionLine().getCardAt(0).isPresent());
        assertEquals("Hermine", opponent.getPrevisionLine().getCardAt(0).get().getName(),"Un emplacement occupé ne devrait pas être écrasé");
        assertEquals(1, opponent.getDeckSize(),"Le Loup devrait rester dans le deck car l'emplacement 0 était déjà occupé");
    }

    @Test
    public void placePrevisionCard_emptyDeckThrows() {
        Opponent opponent = opponentWith();

        assertThrows(NoSuchElementException.class,() -> opponent.placePrevisionCard(0),"Devrait lancer une exception si le deck est vide");
    }

    @Test
    public void placePrevisionCard_multiplePositions() {
        Opponent opponent = opponentWith(hermine(), loup(), louveteau());

        opponent.placePrevisionCard(0);
        opponent.placePrevisionCard(2);
        opponent.placePrevisionCard(3);

        assertTrue(opponent.getPrevisionLine().getCardAt(0).isPresent());
        assertTrue(opponent.getPrevisionLine().getCardAt(2).isPresent());
        assertTrue(opponent.getPrevisionLine().getCardAt(3).isPresent());
        assertFalse(opponent.getPrevisionLine().getCardAt(1).isPresent(),"La position 1 devrait rester vide");
    }

    // applyPrevision

    @Test
    public void applyPrevision_movesCardFromPrevisionToAttack() {
        Opponent opponent = opponentWith(hermine());
        opponent.placePrevisionCard(1);

        opponent.applyPrevision();

        assertTrue(opponent.getBoardLine().getCardAt(1).isPresent(),"La carte devrait être sur le plateau d'attaque après applyPrevision");
        assertFalse(opponent.getPrevisionLine().getCardAt(1).isPresent(),"La ligne de prévision devrait être vide après applyPrevision");
    }

    @Test
    public void applyPrevision_doesNotOverwriteOccupiedAttackSlot() {
        BoardLine attackBoard = new BoardLine();
        BoardLine previsionBoard = new BoardLine();

        ArrayList<Animal> deck = new ArrayList<>();
        deck.add(hermine());
        deck.add(loup());
        Opponent opponent = new Opponent(deck, attackBoard, previsionBoard);

        attackBoard.placeCardAt(hermine(), 0);
        opponent.placePrevisionCard(0);

        opponent.applyPrevision();

        assertEquals("Hermine", attackBoard.getCardAt(0).get().getName(),"Un emplacement occupé sur le plateau d'attaque ne devrait pas être écrasé par la prévision");
        assertTrue(previsionBoard.getCardAt(0).isPresent(),"Le Loup devrait rester sur la ligne de prévision car l'emplacement d'attaque était occupé");
    }

    @Test
    public void applyPrevision_emptyPrevisionDoesNothing() {
        Opponent opponent = opponentWith(hermine());

        opponent.applyPrevision();

        for (int i = 0; i < 4; i++) {
            assertFalse(opponent.getBoardLine().getCardAt(i).isPresent(),"Le plateau d'attaque devrait être vide si la prévision était vide");
        }
    }

    @Test
    public void applyPrevision_allSlotsTransferred() {
        Opponent opponent = opponentWith(hermine(), loup(), louveteau(), elan());

        opponent.placePrevisionCard(0);
        opponent.placePrevisionCard(1);
        opponent.placePrevisionCard(2);
        opponent.placePrevisionCard(3);

        opponent.applyPrevision();

        for (int i = 0; i < 4; i++) {
            assertTrue(opponent.getBoardLine().getCardAt(i).isPresent(),"Tous les emplacements devraient être transférés sur le plateau d'attaque");
            assertFalse(opponent.getPrevisionLine().getCardAt(i).isPresent(),"La ligne de prévision devrait être vide après le transfert complet");
        }
    }

    // opponentAction

    @Test
    public void opponentAction_appliesPrevisionAndPreparesNext() {
        Opponent opponent = opponentWith(hermine(), loup());

        opponent.placePrevisionCard(0);

        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(2);

        opponent.opponentAction(positions);

        assertTrue(opponent.getBoardLine().getCardAt(0).isPresent(),"L'Hermine devrait être sur le plateau d'attaque après opponentAction");
        assertEquals("Hermine", opponent.getBoardLine().getCardAt(0).get().getName());
        assertTrue(opponent.getPrevisionLine().getCardAt(2).isPresent(),"Le Loup devrait être sur la ligne de prévision en position 2");
    }

    @Test
    public void opponentAction_emptyPositionsOnlyAppliesPrevision() {
        Opponent opponent = opponentWith(hermine());
        opponent.placePrevisionCard(1);

        opponent.opponentAction(new ArrayList<>());

        assertTrue(opponent.getBoardLine().getCardAt(1).isPresent(),"La prévision devrait quand même être appliquée même sans nouvelles positions");
        assertEquals(0, opponent.getDeckSize(),"Le deck devrait être vide après la pioche pour la prévision");
    }

    // obstacles sur le plateau d'attaque

    @Test
    public void applyPrevision_respectsObstaclesOnAttackBoard() {
        BoardLine attackBoard = new BoardLine();
        BoardLine previsionBoard = new BoardLine();

        ObstacleStock obstacleStock = new ObstacleStock();
        Obstacle sapin = obstacleStock.getObstacle(ObstacleStock.SAPIN).get();
        attackBoard.placeCardAt(sapin, 0);

        ArrayList<Animal> deck = new ArrayList<>();
        deck.add(hermine());
        Opponent opponent = new Opponent(deck, attackBoard, previsionBoard);

        opponent.placePrevisionCard(0);
        opponent.applyPrevision();

        assertFalse(attackBoard.getCardAt(0).get().isSacrificable(),"L'obstacle ne devrait pas être écrasé par une carte de prévision");
        assertEquals("Sapin", attackBoard.getCardAt(0).get().getName());
    }

    // Croissance sur prévision vs attaque

    @Test
    public void growth_doesNotTransformOnPrevisionLine() {
        Opponent opponent = opponentWith(louveteau());
        opponent.placePrevisionCard(0);

        opponent.getBoardLine().notifyTurnStarted();
        opponent.getBoardLine().notifyTurnStarted();

        assertTrue(opponent.getPrevisionLine().getCardAt(0).isPresent());
        assertEquals("Louveteau",
                opponent.getPrevisionLine().getCardAt(0).get().getName(),"Le Louveteau sur la ligne de prévision ne devrait pas se transformer");
    }

    @Test
    public void growth_transformsAfterMovingToAttackBoard() {
        Opponent opponent = opponentWith(louveteau());
        opponent.placePrevisionCard(0);

        opponent.applyPrevision();

        assertTrue(opponent.getBoardLine().getCardAt(0).isPresent());
        assertEquals("Louveteau", opponent.getBoardLine().getCardAt(0).get().getName(),"Devrait encore être un Louveteau juste après le transfert");

        opponent.getBoardLine().notifyTurnStarted(); // tour 1
        opponent.getBoardLine().notifyTurnStarted(); // tour 2 → transformation

        assertEquals("Loup", opponent.getBoardLine().getCardAt(0).get().getName(),"Le Louveteau devrait se transformer en Loup après 2 tours sur le plateau d'attaque");
    }
}