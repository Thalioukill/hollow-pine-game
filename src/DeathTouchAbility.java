public class DeathTouchAbility extends Ability {
    @Override
    public void onAttack(Card target) {
        //plus de hp point = death
        target.takeDamage(target.getHealthPoints());
    }

    public String getName() { return "Contact MORTEL"; }
}

