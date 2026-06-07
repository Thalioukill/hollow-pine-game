public class StinkyAbility extends Ability{
    @Override
    public int modifyIncomingDamage(int damage) {
        // reduire damage par 1
        return damage - 1;
    }

    @Override
    public Ability cloneAbility() { return new StinkyAbility(); }

    public String getName() { return "Puant"; }

    public String getShortName() { return "Puant"; }


}

