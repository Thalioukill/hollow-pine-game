public class MultipleLivesAbility extends Ability{
    @Override
    public boolean onSacrifice(Animal self) {
        self.setHealthPoints(1); // revive
        return true;
    }

    public String getName() { return "Nombreuses vies"; }
}