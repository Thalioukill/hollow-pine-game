public class RunnerAbility extends Ability{


    /*
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
     */

    @Override
    public void afterAttack(Animal self, int currentPos, BoardLine line) {
        System.out.println("DEBUG Runner: pos=" + currentPos
                + " droite=" + (currentPos+1) + " occupée=" + line.isOccupied(currentPos+1)
                + " gauche=" + (currentPos-1) + " occupée=" + (currentPos > 0 && line.isOccupied(currentPos-1)));

        int droite = currentPos + 1;
        int gauche = currentPos - 1;

        if (droite < 4 && !line.isOccupied(droite)) {
            line.moveCard(currentPos, droite);
            System.out.println("DEBUG Runner: déplacé vers droite=" + droite);
        } else if (gauche >= 0 && !line.isOccupied(gauche)) {
            line.moveCard(currentPos, gauche);
            System.out.println("DEBUG Runner: déplacé vers gauche=" + gauche);
        } else {
            System.out.println("DEBUG Runner: bloqué !");
        }
    }

    public String getName() { return "Coureur"; }
}
