public abstract class Ability {

    public abstract String getName();

    // Moment : Après avoir attaqué (Élan / Coureur) =>
    protected void afterAttack(Animal self, int currentPos, BoardLine line) {
    }
    //StinkyAbility
    protected int modifyIncomingDamage(int damage) {return damage;}

    //manylives => ManyLivesAbili
    protected boolean onSacrifice(Animal self) {
        return false;
    }

    //Porc-épic => SharpQuills
    protected void onHit(Animal self, Animal attacker) {
    }

    //Poison => PoisonAbility
    protected void onAttack(Card card){

    }
    // flying ability...
    protected boolean bypassTarget(){
        return false;
    }

    // Moment : Fin de tour / Début de tour (Louveteau / Croissance)=>
    protected void onTurnUpdate(Animal self, int position, BoardLine line) {

    }
}