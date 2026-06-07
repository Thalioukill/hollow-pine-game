package model.abilities;

import model.cards.Animal;

public class MultipleLivesAbility extends Ability{
    @Override
    public boolean onSacrifice(Animal self) {
        self.setHealthPoints(1); // revive
        return true;
    }

    @Override
    public Ability cloneAbility() { return new MultipleLivesAbility(); }

    public String getName() {
        return "Nombreuses vies";
    }

    public String getShortName() {
        return "Vies ++";
    }

}