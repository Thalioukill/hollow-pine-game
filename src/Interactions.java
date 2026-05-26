import java.util.Optional;
import java.util.Scanner;

//Gère exclusivement les entrées (Scanner) et sorties (System.out) console.
public class Interactions {
    private final Scanner m_scanner;

    public Interactions() {
        // on n'instancie qu'un seul Scanner pour System.in
        this.m_scanner = new Scanner(System.in);
    }

    // Demande à l'utilisateur de choisir sa prochaine action via un menu et retourne membre de l'énumération Actions validé.
    public void askNextAction(User human) {
        boolean test = true;
        while (test) {
            System.out.println("\n--- ACTIONS POSSIBLES ---");
            System.out.println("[P] Placer une carte sur le plateau\n");
            System.out.println("[F] Finir le tour");
            System.out.print("Votre choix : ");

            String input = m_scanner.next().toUpperCase(); // On tolère 'p' ou 'P
            if (input.equals("P")){
                char letter = this.askCardLetter(human.m_hand.getSize());
                Optional<Animal> optAnimal = human.m_hand.getCard(letter);

                if (optAnimal.isEmpty()) {
                    this.displayMessage("Carte inexistante.");
                    return;
                }

                Animal a = optAnimal.get();

                int remainingBlood = a.getBloodCost();
                // si l'animal est un écureuil => on a pas besoin de sacrifier quoi que ce soit pour le poser.
                if(!(remainingBlood == 0 && a.getBoneCost() == 0)){
                    if(canAfford(a, human)){
                        // faire les sacrifices, puis ensuite demander la position à l'utiliateur.
                        if (remainingBlood > 0) {
                            for (int i = 0; i < remainingBlood; i++) {
                                this.displayMessage("Sacrifice " + (i + 1) + "/" + remainingBlood + " :");
                                int sacrificePos = this.askPositionSacrifice();

                                Optional<Card> optTarget = m_boardLine.getCardAt(sacrificePos);

                                // On vérifie si l'emplacement contient bien une carte
                                if (optTarget.isPresent()) {
                                    Card target = optTarget.get();

                                    // On vérifie si elle est sacrifiable (Obstacle ou pas ?)
                                    if (target.isSacrificable()) {
                                        m_boardLine.removeCardAt(sacrificePos);
                                        m_bonesToken++; // Règle : +1 os par sacrifice
                                        this.displayMessage("Emplacement libéré. +1 os. +1 goutte de sang.");
                                    } else {
                                        this.displayMessage("Impossible : un obstacle ne peut pas être sacrifié !");
                                        i--; // On redemande cette goutte de sang
                                    }
                                } else {
                                    // CAS MANQUANT : L'emplacement est vide
                                    this.displayMessage("Erreur : Il n'y a aucune carte en position " + sacrificePos + ".");
                                    i--; // IMPORTANT: On décrémente pour forcer l'utilisateur à refaire ce sacrifice
                                }
                            }
                        }
                    }

                }
                return;
            }

            if(canAfford(a, human)){
                   /*
                    int position = this.askPosition();
                    int bloodAlreadyPaid = 0;
                    if (!prepareSlot(position, bloodAlreadyPaid, human)){
                        return;
                    }
                    */
            }
            return;


        }

        if (input.equals("F")){
            return ;
        }
        // Sinon la saisie est invalide...
        System.out.println("Choix inconnu ! Veuillez saisir P ou F.");
    }

        private boolean prepareSlot(int pos, int bloodAlreadyPaid, User human) {
            Optional<Card> optTarget = human.m_boardLine.getCardAt(pos);

            // Si l'emplacement est vide, rien à faire = on peut poser la carte !
            if (optTarget.isEmpty()) {
                return true;
            }

            Card target = optTarget.get();

            // Obstacle ?
            if (!target.isSacrificable()) {
                this.displayMessage("L'emplacement est occupé par un " + target.getName() + " (Obstacle incassable).");
                return false; // On ne peut pas poser ici
            }

            // Si pas obstacle => demander à l'utilisateur si il veut sacrifier cette carte pour poser la sienne à la place
            boolean confirm = this.askConfirmation("L'emplacement est occupé par " + target.getName() + ". Le sacrifier pour libérer la place ?");

            if (confirm) {
                human.m_boardLine.removeCardAt(pos);
                bloodAlreadyPaid = 1;
                human.m_bonesToken++; // Règle : tout sacrifice donne +1 os
                this.displayMessage("Sacrifice de " + target.getName() + " pour libérer la place (+1 sang).");
                this.displayMessage("Emplacement libéré. +1 os.");
                return true; // L'emplacement est maintenant libre, on renvoie true !
            }

            return false; // L'utilisateur a annulé, on ne peut pas poser
        }

        private boolean askConfirmation(String s) {
            while (true) {
                System.out.print(s);
                String input = m_scanner.next().toUpperCase();
                if (input.equals("OUI")){
                    return true;
                } else {
                    m_scanner.next(); // Lit le prochain mot
                }
                // ERREURRRR
                System.out.println("Veuillez saisir OUI ou NON");
            }
        }


        // Demande une position sur le plateau (chifres 1 à 4).
        public int askPosition() {
            while (true) {
                System.out.print("Entrez le numéro de l'emplacement où vous souhaitez poser la carte (1, 2, 3 ou 4) : ");
                if (m_scanner.hasNextInt()) {
                    int pos = m_scanner.nextInt();
                    if (pos >= 1 && pos <= 4) {
                        return pos;
                    }
                } else {
                    m_scanner.next(); // Lit le prochain mot
                }
                // ERREURRRR
                System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
            }
        }

        public int askPositionSacrifice() {
            while (true) {
                System.out.print("Entrez le numéro de l'emplacement de la carte que vous souhaitez sacrifier (1, 2, 3 ou 4) : ");
                if (m_scanner.hasNextInt()) {
                    int pos = m_scanner.nextInt();
                    if (pos >= 1 && pos <= 4) {
                        return pos;
                    }
                } else {
                    m_scanner.next(); // Lit le prochain mot
                }
                // ERREURRRR
                System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
            }
        }



        // Demande la lettre d'une carte en main (lettres A, B, C...) + handSize taille actuelle de la main pour limiter le chiox
        public char askCardLetter(int handSize) {
            char maxLetter = (char) ('A' + handSize - 1);
            while (true) {
                System.out.print("Choisissez une carte dans votre main (lettre entre A et " + maxLetter + ") : ");
                String input = m_scanner.next().toUpperCase();

                if (input.length() == 1) {
                    char letter = input.charAt(0);
                    if (letter >= 'A' && letter <= maxLetter) {
                        return letter;
                    }
                }
                System.out.println("Lettre incorrecte ! Votre main va de A à " + maxLetter + ".");
            }
        }

        private boolean canAfford(Animal card, User human) {
            if (card.getBoneCost() > 0 && human.m_bonesToken < card.getBoneCost()) {
                this.displayMessage("Pas assez d'os (" + human.m_bonesToken + "/" + card.getBoneCost() + ").");
                return false;
            }
            if (card.getBloodCost() > 0 && human.m_boardLine.getSacrificableCount() < card.getBloodCost()) {
                this.displayMessage("Pas assez de créatures à sacrifier pour poser cette carte, veuillez en choisir une autre coutant moins de "+ human.m_boardLine.getSacrificableCount());
                return false;
            }
            return true;
        }


        // Affiche un message simple (dégâts, pioche,etc)
        public void displayMessage(String message) {
            System.out.println(message);
        }
}
