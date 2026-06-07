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
        if (m_turnsOnBoard >= 2) { // se transforme seulement au début du deuxième tour
            line.replaceCard(position, m_finalForm.cloneAnimal());
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

}
