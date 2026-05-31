public class SharpQuillsAbility extends Ability{
    @Override
    public void onHit(Animal self, Animal attacker) {
        attacker.takeDamage(1); // riposte 1 dégât
    }
}
