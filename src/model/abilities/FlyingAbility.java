package model.abilities;

public class FlyingAbility extends Ability {
    @Override
    public boolean bypassTarget(){
        return true;
    }

    @Override
    public Ability cloneAbility() { return new FlyingAbility(); }

    public String getName() { return "Volant"; }

    public String getShortName() { return "Volant"; }

}

