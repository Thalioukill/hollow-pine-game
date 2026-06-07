package model.abilities;

import model.cards.Card;

public class DeathTouchAbility extends Ability {
    @Override
    public void onAttack(Card target) {
        //plus de hp point = death
        target.takeDamage(target.getHealthPoints());
    }

    @Override
    public Ability cloneAbility() { return new DeathTouchAbility(); }

    public String getName() { return "Contact MORTEL"; }

    public String getShortName() { return "Contact ☠"; }

}

