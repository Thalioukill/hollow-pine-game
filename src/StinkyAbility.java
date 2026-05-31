public class StinkyAbility extends Ability{
    @Override
    public int modifyIncomingDamage(int damage) {
        // reduire damage par 1
        return damage - 1;
    }

    public String getName() { return "Puant"; }

}

