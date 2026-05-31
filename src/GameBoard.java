import java.util.ArrayList;
import java.util.Optional;

public class GameBoard {
    private ArrayList<String>[] creationAffichageCartes(ArrayList<Optional<Animal>> listeCarte){
        ArrayList<String>[] cartes = new ArrayList[4];
        for (int i=0; i<4; i++){
            cartes[i] = new ArrayList<>();
            Optional<Card> optCard = listeCarte.get(i);

            if (optCard.isEmpty()) {
                remplirSlotVide(cartes[i]);
            } else {
                remplirSlotOccupe(cartes[i], optCard.get());
            }
        }
        return cartes;
    }

    private void remplirSlotVide(ArrayList<String> lines) {
        lines.add("*************");
        for (int j = 0; j < 5; j++) {
            lines.add("*           *");
        }
        lines.add("*************");
    }

    private void remplirSlotOccupe(ArrayList<String> lines, Card card) {
        lines.add("*-----------*");
        lines.add("| " + String.format("%-9s", card.getName()) + " |");
        lines.add("|-----------|");
        lines.add(String.format("| PV: %-6d |", card.getHealthPoints()));

        // Le polymorphisme choisit la bonne version (Animal ou Obstacle)
        //lines.addAll(card.getSpecificDisplayLines());

        lines.add("*-----------*");
}


    private void affichageCarte(ArrayList<String>[] cartes){
        for (int i=0; i<7; i++){
            System.out.print("      ");
            for (ArrayList<String> c : cartes){
                System.out.print(c.get(i) + "   ");
            }
            System.out.println();
        }
    }
    public void affichage(int partie, int partiesGagneesJoueur, int tour, BoardLine emplacementJ2, BoardLine emplacementTour, BoardLine emplacementJ1, int score, int os, ArrayList<Animal> main, ArrayList<Animal> pioche){
        System.out.println("Partie "+partie+" | Gagnées : "+partiesGagneesJoueur+" | Perdues : "+ (partie-partiesGagneesJoueur - 1));
        System.out.println("Tour n° "+tour);

        ArrayList<String>[] cartesAdv;
        cartesAdv = new ArrayList[4];
        cartesAdv = creationAffichageCartes(emplacementJ2);

        ArrayList<String>[] cartesTour;
        cartesTour = new ArrayList[4];
        cartesTour = creationAffichageCartes(emplacementTour);

        ArrayList<String>[] cartesJoueur;
        cartesJoueur = new ArrayList[4];
        cartesJoueur = creationAffichageCartes(emplacementJ1);

        affichageCarte(cartesAdv);

        System.out.println("            ||              ||              ||              ||");
        System.out.println("            \\/              \\/              \\/              \\/");

        affichageCarte(cartesTour);

        System.out.println("Balance : " + score);
        System.out.println("Os : " + os);

        affichageCarte(cartesJoueur);

        System.out.println("Votre main : ");

        int n = 0;
        for (Animal a : main){
            n++;
            System.out.print("\n"+n+". "+a.getName()+" PV: "+a.getHealthPoints()+" | Atk: "+a.getAttackPoints()+" | Gouttes de sang: "+a.getBloodCost()+" | Os : "+a.getBoneCost()+"\n");
        }
        System.out.println();

        System.out.println("Pioche");


        System.out.println("*-----------*");
        System.out.println("|           |");
        System.out.println("|           |");
        if (pioche.size() < 10) //Pour prévenir le décalage de la barre de fin de ligne si la taille ne tient que sur un caractère, il y a un espace en moins dans le else
        {
            System.out.println("|    "+pioche.size()+"      |");
        }
        else
        {
            System.out.println("|    "+pioche.size()+"     |");
        }
        System.out.println("|   cartes  |");
        System.out.println("|           |");
        System.out.println("*-----------*");
    }

}
