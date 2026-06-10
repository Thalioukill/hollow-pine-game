package model.abilities;

import model.board.BoardLine;
import model.cards.Animal;

public class GrowthAbility extends Ability{
    private final Animal m_finalForm;
    private int m_turnsOnBoard = 0;

    public GrowthAbility(Animal loup) {
        this.m_finalForm = loup;
    }

    @Override
    public void onTurnUpdate(Animal self, int position, BoardLine line) {
        m_turnsOnBoard++;
        if (m_turnsOnBoard >= 2) {
            Animal evolved = m_finalForm.cloneAnimal();
            // Copie toutes les abilities acquises sauf GrowthAbility elle-même comme ca si on l'ajoute à une autre carte avec une ability elle la garde.
            for (Ability a : self.getAbilities()) {
                if (a.getType() != AbilityType.GROWTH) {
                    evolved.addAbility(a.cloneAbility());
                }
            }
            line.replaceCard(position, evolved);
        }
    }

    @Override
    public Ability cloneAbility() {
        return new GrowthAbility(m_finalForm.cloneAnimal());
    }

    public String getName(){
        return "Croissance";
    }

    public String getShortName() { return "Grandir"; }

    @Override
    public AbilityType getType() { return AbilityType.GROWTH; }

}
