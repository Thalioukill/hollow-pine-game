import model.abilities.*;
import model.board.BoardLine;
import model.cards.Animal;
import model.cards.AnimalType;
import model.cards.Obstacle;
import model.cards.ObstacleType;
import model.stock.AnimalStock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    private Animal animal(String name, int atk, int hp, int blood, int bone, AnimalType type) {
        return new Animal(name, atk, hp, blood, bone, type);
    }

    private Animal hermine() { return new AnimalStock().getCard(AnimalType.HERMINE).get(); }
    private Animal loup() { return new AnimalStock().getCard(AnimalType.LOUP).get(); }

    // test sur AnimalStock

    @Test
    public void animalStock_getCardReturnsCopy() {
        Animal a = loup();
        Animal b = loup();

        a.takeDamage(1);

        assertEquals(2, b.getHealthPoints(), "Modifier 1 instance ne devrait pas modifier une autre (on vérifie le clonage d'un Animal.)");
    }

    @Test
    public void cardAttack_doesNotGoBelowZero() {
        Animal attacker = loup();
        Animal target = animal("Ecureuil", 0, 1, 0, 0, AnimalType.ECUREUIL);

        attacker.attack(target);

        assertTrue(target.isDead(), "l'Ecureuil devrait etre mort");
    }

    @Test
    public void cardAttack_killedCardIsDetected() {
        Animal attacker = loup();
        Animal target = hermine();

        assertFalse(target.isDead(), "L'Hermine devrait etre vivante avant l'attaque");
        attacker.attack(target);
        assertTrue(target.isDead(), "l'Hermine devrait etre morte après l'attaque du Loup");
    }

    @Test
    public void cardAttack_obstacleAbsorbsDamage() {
        Animal attacker = loup();
        Obstacle obstacle = new Obstacle("Sapin", 3, ObstacleType.SAPIN);

        attacker.attack(obstacle);

        assertEquals(0, obstacle.getHealthPoints(),"Le Sapin devrait avoir 0 HP après l'attaque du Loup (3 ATK)");
        assertTrue(obstacle.isDead());
    }
}