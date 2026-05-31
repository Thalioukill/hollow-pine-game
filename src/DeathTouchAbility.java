public class DeathTouchAbility extends Ability {
    @Override
    public void afterAttack(Animal attacker, Animal target, Player defenderOwner, Game game) {
        //plus de hp point = death
        target.takeDamage(target.getHealthPoints());
    }
}

