public class StinkyAbility extends Ability{
    public StinkyAbility(){
    }
    @Override
    public int modifyIncomingDamage(int damage) {
        // reduire damage par 1
        return damage - 1;
    }

}

