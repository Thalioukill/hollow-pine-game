public class GameBoard {
    private ArrayList<String>[] creationAffichageCartes(Optional<Animal>[] listeCarte){
        ArrayList<String>[] cartes;
        cartes = new ArrayList[4];
        for (int i=0; i<4; i++){
            cartes[i] = new ArrayList<>();
            if (listeCarte[i].isEmpty()){
                cartes[i].add("*************");
                for (int j = 0; j < 5; j++){
                    cartes[i].add("*           *");
                }
                cartes[i].add("*************");
            }
            else {
                cartes[i].add("*-----------*");

                String nom = listeCarte[i].get().getNom();
                while (nom.length() < 9){
                    nom += " ";
                }
                cartes[i].add("| "+nom+" |");
                cartes[i].add("|-----------|");
                cartes[i].add("| PV: "+listeCarte[i].get().getPv()+"     |");
                if (listeCarte[i].get().getAtk().isPresent()){
                    cartes[i].add("| Atk: " + listeCarte[i].get().getAtk().get() + "    |");
                    if (listeCarte[i].get().getEstVolant()){
                        cartes[i].add("| Volant    |");
                    }
                    else cartes[i].add("|           |");
                }
                else {
                    cartes[i].add("|           |");
                    cartes[i].add("|           |");
                }

                cartes[i].add("*-----------*");


            }
        }
        return cartes;
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
    public void affichage(int partie, int partiesGagneesJoueur, int tour, Optional<Animal>[] emplacementJ2, Optional<Animal>[] emplacementTour, Optional<Animal>[] emplacementJ1, int score, int os, ArrayList<Animal> main, ArrayList<Animal> pioche){
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
            System.out.print("\n"+n+". "+a.getNom()+" PV: "+a.getPv()+" | Atk: "+a.getAtk().get()+" | Gouttes de sang: "+a.getSangRequis()+" | Os : "+a.getOsRequis()+"\n");
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

        System.out.println("Actions possibles: \n" +
                "  [fin] Terminer votre tour\n" +
                "  [piocher] Piocher une carte\n" +
                "  [placer <numero carte> <position>] Placer une carte sur le plateau\n" +
                "\n");
    }

}
