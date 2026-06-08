import model.board.BoardLine;
import model.cards.Animal;
import model.stock.AnimalStock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbilitiesTest {

    // test sur les ABILITIES
    private Animal animal(String name, int atk, int hp, int blood, int bone) {
        return new Animal(name, atk, hp, blood, bone);
    }

    private Animal hermine() { return new AnimalStock().getCard(AnimalStock.HERMINE).get(); }
    private Animal loup() { return new AnimalStock().getCard(AnimalStock.LOUP).get(); }
    private Animal louveteau() { return new AnimalStock().getCard(AnimalStock.LOUVETEAU).get(); }
    private Animal corbeau() { return new AnimalStock().getCard(AnimalStock.CORBEAU).get(); }
    private Animal punaise() { return new AnimalStock().getCard(AnimalStock.PUNAISE).get(); }
    private Animal elan() { return new AnimalStock().getCard(AnimalStock.ELAN).get(); }
    private Animal vipere() { return new AnimalStock().getCard(AnimalStock.VIPERE).get(); }
    private Animal grizzly() { return new AnimalStock().getCard(AnimalStock.GRIZZLY).get(); }
    private Animal porcEpic() { return new AnimalStock().getCard(AnimalStock.PORC_EPIC).get(); }
    private Animal chat() { return new AnimalStock().getCard(AnimalStock.CHAT).get(); }


    @Test
    public void ability_cloneAbilityIsDeep() {
        Animal a = louveteau();
        Animal b = louveteau();

        BoardLine lineA = new BoardLine();
        BoardLine lineB = new BoardLine();
        lineA.placeCardAt(a, 0);
        lineB.placeCardAt(b, 0);

        lineA.notifyTurnStarted();
        lineA.notifyTurnStarted();

        assertEquals("Louveteau", lineB.getCardAt(0).get().getName(),"Le louveteau b ne devrait pas se transformer meme si le louveteau a grandi => clone de Ability !");
    }

    // MultipleLivesAbility
    @Test
    public void multipleLives_survivesSacrifice() {
        Animal c = chat();
        assertTrue(c.survivesSacrifice(), "Le chat doit survivre au sacrifice");
        assertEquals(1, c.getHealthPoints(), "Apres le sacrifice le chat devrait avoir 1 HP");
    }

    @Test
    public void multipleLives_normalAnimalDoesNotSurvive() {
        assertFalse(hermine().survivesSacrifice(), "Un animal normal ne devrait pas survivre au sacrifice");
    }

    // FlyingAbility
    @Test
    public void flying_bypassesTarget() {
        assertTrue(corbeau().isTargetBypassed(),"La carte devrait ignorer la carte en face d'elle et voler.");
    }

    @Test
    public void flying_dealsDirecDamageOverBlocker() {
        BoardLine attackerLine = new BoardLine();
        BoardLine defenderLine = new BoardLine();

        Animal c = corbeau();
        attackerLine.placeCardAt(c, 0);
        defenderLine.placeCardAt(loup(), 0);

        int directDamage = attackerLine.attack(defenderLine);

        assertEquals(2, directDamage,"Le volant doit infliger 2 points de dégats DIRECTS, et pas les infliger sur la carte en face.");
        assertEquals(2, defenderLine.getCardAt(0).get().getHealthPoints(),"La carte en face du volant ne devrait pas prendre de dégats.");
    }

    // StinkyAbility
    @Test
    public void stinky_reducesDamageByOne() {
        Animal p = punaise();
        loup().attack(p);

        assertEquals(0, p.getHealthPoints(),"La Punaise a 2HP mais elle enlève 1 point d'attaque au loup qui en a 3, donc elle a 0 HP");
    }

    @Test
    public void stinky_oneAttackDoesSingleDamage() {
        Animal p = punaise();
        hermine().attack(p);

        assertEquals(2, p.getHealthPoints(),"La Punaise attaquée par un animal possédant 1 point de dégat doit recevoir 0 points de dégats");
    }

    // RunnerAbility
    @Test
    public void runner_movesRightAfterAttack() {
        BoardLine line = new BoardLine();
        Animal e = elan();
        line.placeCardAt(e, 0);

        line.attack(new BoardLine());

        assertTrue(line.getCardAt(0).isEmpty(),"l'Élan devrait avoir bougé sur la case 1 (Ability Runner)");
        assertTrue(line.getCardAt(1).isPresent(),"Si la case à droite de l'Élan est vide, après son attaque il doit bouger d'une case vers la droite.");
    }

    @Test
    public void runner_movesLeftIfRightIsOccupied() {
        BoardLine line = new BoardLine();
        Animal e = elan();
        line.placeCardAt(e, 1);
        line.placeCardAt(hermine(), 2);

        line.attack(new BoardLine());

        assertTrue(line.getCardAt(1).isEmpty(),"l'Élan devrait se déplacer à gauche car l'emplacement 2 à sa droite est occupé par une Hermine.");
        assertTrue(line.getCardAt(0).isPresent(),"l'Élan devrait bouger à gauche...");
    }

    @Test
    public void runner_doesNotMoveWhenBlocked() {
        BoardLine line = new BoardLine();
        Animal e = elan();
        line.placeCardAt(e, 1);
        line.placeCardAt(hermine(), 2);
        line.placeCardAt(vipere(), 0);

        line.attack(new BoardLine());

        assertTrue(line.getCardAt(1).isPresent(),"L'Élan ne devrait pas bouger si les deux côtés sont bloqués.");
        assertEquals("Élan", line.getCardAt(1).get().getName(),"La carte en position 1 doit toujours être l'Élan et pas une autre.");
    }

    // DeathTouchAbility
    @Test
    public void deathTouch_killsAnyCard() {
        Animal v = vipere();
        Animal g = grizzly();

        v.attack(g);

        assertTrue(g.isDead(),"DeathTouch devrait tuer toutes les cartes touchées peut importe leurs HP");
    }

    // SharpQuillsAbility
    @Test
    public void sharpQuills_counterattacks() {
        Animal p = porcEpic();
        Animal l = loup();
        l.attack(p);

        assertEquals(1, l.getHealthPoints(),"Loup (2 PV) devrait subir une contre-attaque de SharpQuills → il lui reste 1 PV");
    }

    // GrowthAbility
    @Test
    public void growth_doesNotTransformOnFirstTurn() {
        BoardLine line = new BoardLine();
        Animal l = louveteau();
        line.placeCardAt(l, 0);

        line.notifyTurnStarted();

        assertTrue(line.getCardAt(0).isPresent());
        assertEquals("Louveteau", line.getCardAt(0).get().getName(),"Louveteau ne devrait pas se transformer au premier tour");
    }

    @Test
    public void growth_transformsOnSecondTurn() {
        BoardLine line = new BoardLine();
        Animal l = louveteau();
        line.placeCardAt(l, 0);

        line.notifyTurnStarted();
        line.notifyTurnStarted();

        assertTrue(line.getCardAt(0).isPresent());
        assertEquals("Loup", line.getCardAt(0).get().getName(),"Le Louveteau devrait se transformer en Loup au second tour.");
        assertEquals(3, line.getCardAt(0).get().getAttackPoints(),"Le Loup devrait avoir 3 ATK après sa transformation");
    }

    @Test
    public void growth_cloneAbilityHasFreshCounter() {
        Animal l = louveteau();
        Animal clone = l.cloneAnimal();
        BoardLine line = new BoardLine();
        line.placeCardAt(clone, 0);

        line.notifyTurnStarted();

        assertEquals("Louveteau", line.getCardAt(0).get().getName(),"Le Louveteau cloné ne devrait pas se transformer au tour 1 (le compteur doit être remis à zéro)");
    }
}
