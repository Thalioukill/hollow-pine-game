import java.util.Optional;

public abstract class Ability {

    // Moment : Après avoir attaqué (Élan / Coureur) =>
    void afterAttack(Animal self, int currentPos, BoardLine line) {
    }
    //StinkyAbility
    int modifyIncomingDamage(int damage) {return damage;}

    //manylives => ManyLivesAbili
    boolean onSacrifice(Animal self) {
        return false;
    }

    //Porc-épic => SharpQuills
    void onHit(Animal self, Animal attacker) {
    }

    //Poison => PoisonAbility
    void onKill(Card card){

    }
    // flying ability...
    boolean bypassTarget(){
        return false;
    }

    // Moment : Fin de tour / Début de tour (Louveteau / Croissance)=>
    void onTurnUpdate(Animal self, int position, BoardLine line) {

    }
/*
        // Moment : Pendant un sacrifice (Chat)
    default boolean onSacrificed(Animal self) { return false; }

    // Moment : Après avoir attaqué (Élan / Coureur)
    default void afterAttack(Animal self, int currentPos, BoardLine line) {}

    // Moment : Fin de tour / Début de tour (Louveteau / Croissance)
    default void onTurnUpdate(Animal self, BoardLine line) {}

 */
}