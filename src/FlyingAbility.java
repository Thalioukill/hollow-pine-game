public class FlyingAbility extends Ability {
    @Override
    public boolean bypassTarget(){
        return true;
    }

    public String getName() { return "Volant"; }

}

