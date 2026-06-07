package model.abilities;

import model.cards.Animal;

public class SharpQuillsAbility extends Ability {
    @Override
    public void onHit(Animal self, Animal attacker) {
        attacker.takeDamage(1); // riposte 1 dégât
    }

    @Override
    public Ability cloneAbility() { return new SharpQuillsAbility(); }

    public String getName() { return "Piques aiguisés"; }

    public String getShortName() { return "Piques ⚔"; }
}
