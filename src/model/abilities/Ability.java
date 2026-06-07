package model.abilities;

import model.board.BoardLine;
import model.cards.Animal;
import model.cards.Card;

public abstract class Ability {

    public abstract String getName();

    // Moment : Après avoir attaqué (Élan / Coureur)
    public void afterAttack(Animal self, int currentPos, BoardLine line) {
    }
    //abilities.StinkyAbility
    public int modifyIncomingDamage(int damage) {return damage;}

    //manylives => ManyLivesAbili
    public boolean onSacrifice(Animal self) {
        return false;
    }

    //Porc-épic => SharpQuills
    public void onHit(Animal self, Animal attacker) {
    }

    //Poison => PoisonAbility
    public void onAttack(Card card){

    }
    // flying ability...
    public boolean bypassTarget(){
        return false;
    }

    // Moment : Fin de tour / Début de tour (Louveteau / Croissance)
    public void onTurnUpdate(Animal self, int position, BoardLine line) {

    }

    public abstract Ability cloneAbility();

    public abstract String getShortName(); // ← max 9 caractères pour l'affichage carte
}