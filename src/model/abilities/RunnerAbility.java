package model.abilities;

import model.board.BoardLine;
import model.cards.Animal;

public class RunnerAbility extends Ability {
    @Override
    public void afterAttack(Animal self, int currentPos, BoardLine line) {
        int droite = currentPos + 1;
        int gauche = currentPos - 1;

        // tenter d'aller à droite
        if (droite < 4 && !line.isOccupied(droite)) {
            line.moveCard(currentPos, droite);
        }
        // Sinon, tenter d'aller à gauche
        else if (gauche >= 0 && !line.isOccupied(gauche)) {
            line.moveCard(currentPos, gauche);
        }
        // Sinon, on ne bouge pas
    }

    @Override
    public Ability cloneAbility() { return new RunnerAbility(); }

    public String getName() { return "Coureur"; }

    public String getShortName() { return "Coureur"; }

}
