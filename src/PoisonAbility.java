public class PoisonAbility extends Ability{
    @Override
    public void onKill(Card target) {
        target.takeDamage(target.getHealthPoints()); // tue instantanément
    }
}
