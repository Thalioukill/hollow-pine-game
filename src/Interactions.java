import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

//Gère exclusivement les entrées (Scanner) et sorties (System.out) console.
public class Interactions {
    private final Scanner m_scanner;
    // DISCLAIMER : potentiellement mattre le player en attributs

    public Interactions() {
        // on n'instancie qu'un seul Scanner pour System.in
        this.m_scanner = new Scanner(System.in);
    }

    // Constructeur de test
    public Interactions(Scanner scanner) {
        this.m_scanner = scanner;
    }

    // Demande à l'utilisateur de choisir sa prochaine action via un menu et retourne membre de l'énumération Actions validé.
    public boolean askNextAction(User human) {
        boolean test = true;
        while (test) {
            System.out.println("\n--- ACTIONS POSSIBLES ---");
            if (human.getHandSize() > 0) {
                System.out.println("[P] Placer une carte sur le plateau\n");
            } else {
                System.out.println("Votre main est vide, vous ne pouvez plus placer de carte !");
            }
            System.out.println("[F] Finir le tour");
            System.out.print("Votre choix : ");

            String input = m_scanner.next().toUpperCase(); // On tolère 'p' ou 'P
            if (input.equals("P")) {
                if (human.getHandSize() == 0) {
                    System.out.println("Vous n'avez plus de cartes en main !");
                    continue;
                }
                int handPosition = this.askCardLetter(human.getHandSize(), human);
                Animal handAnimal = human.getHandCard(handPosition);
                int remainingBlood = handAnimal.getBloodCost();
                // si l'animal est un écureuil => on a pas besoin de sacrifier quoi que ce soit pour le poser.
                if (!(handAnimal.getBloodCost() == 0 && handAnimal.getBoneCost() == 0)) {
                    if (canAfford(handAnimal, human)) {
                        // faire les sacrifices, puis ensuite demander la position à l'utiliateur.
                        if (remainingBlood > 0) {
                            for (int i = 0; i < remainingBlood; i++) {
                                this.displayMessage("Sacrifice " + (i + 1) + "/" + remainingBlood + " :");
                                int sacrificePos = this.askPositionSacrifice(human);
                                Card target = human.getBoardLineCardAt(sacrificePos).get();
                                // On vérifie si elle est sacrifiable (Obstacle ou pas ?)
                                if (target.isSacrificable()) {
                                    // si c'est un CHAT -> ne peut pas etre SACRIFIE
                                    if(!target.survivesSacrifice()){
                                        human.sacrifice(sacrificePos);
                                        this.displayMessage("Emplacement libéré. +1 os. +1 goutte de sang.");
                                    }
                                    this.displayMessage("Le chat survit au sacrifice : vous récuprez +1 os. +1 goutte de sang.");
                                } else {
                                    this.displayMessage("Impossible : un obstacle ne peut pas être sacrifié !");
                                    i--; // On redemande cette goutte de sang
                                }
                            }
                        }
                    }

                }
                // On demande la position de la carte...
                int position = this.askPosition(human);
                human.playCardToBoard(handPosition, position);
                return true;
            }
            if (input.equals("F")) {
                return false;
            }
            // Sinon la saisie est invalide...
            System.out.println("Choix inconnu ! Veuillez saisir P ou F.");
        }
        return false;
    }

    public boolean askConfirmation(String s) {
        while (true) {
            System.out.print(s);
            String input = m_scanner.next().toUpperCase();
            if (input.equals("OUI")){
                return true;
            } else if (input.equals("NON")){
                return false;
            }
            // ERREURRRR
            System.out.println("Veuillez saisir OUI ou NON");
        }
    }


    // Demande une position sur le plateau (chifres 1 à 4).

    public int askPosition(User human) {
        while (true) {
            System.out.print("Entrez le numéro de l'emplacement où vous souhaitez poser la carte (1, 2, 3 ou 4) : ");
            if (m_scanner.hasNextInt()) {
                int pos = m_scanner.nextInt();
                int index = toIndex(pos);
                Optional<Card> result = Optional.empty();
                if (pos > 0 && pos <= human.getHandSize()){
                    result = human.getBoardLineCardAt(index);
                }
                if(result.isPresent()){
                    System.out.println("Emplacement déjà occupé, veuillez choisir un autre emplacement");
                    continue;
                }
                else{
                    return index;
                }
            } else {
                m_scanner.next(); // Lit le prochain mot
                // ERREURRRR
                System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
            }
        }
    }

    public int askPositionSacrifice(User human) {
        while (true) {
            System.out.print("Entrez le numéro de l'emplacement de la carte que vous souhaitez sacrifier (1, 2, 3 ou 4) : ");
            if (m_scanner.hasNextInt()) {
                int pos = m_scanner.nextInt();
                if(pos > 0 && pos <= 4){
                    int index = toIndex(pos);
                    Optional<Card> result = human.getCardAt(index);
                    if (result.isPresent()) {
                        if(result.get().isSacrificable()){
                            return index; // on retourne juste l'int
                        }
                    }
                }
                else{
                    System.out.println("Veuillez choisir un chiffre entre 1 et 4...");
                }
            } else {
                m_scanner.next(); // Lit le prochain mot
                // ERREURRRR
                System.out.println("Saisie invalide ! Vous devez entrer un chiffre entier entre 1 et 4.");
            }
        }
    }

    // Demande la lettre d'une carte en main (lettres A, B, C...) + handSize taille actuelle de la main pour limiter le chiox
    public int askCardLetter(int handSize, User human) {
        char maxLetter = (char) ('A' + handSize - 1);
        while (true) {
            System.out.print("Choisissez une carte dans votre main (lettre entre A et " + maxLetter + ") : ");
            String input = m_scanner.next().toUpperCase();

            if (input.length() == 1) {
                char letter = input.charAt(0);

                if (!Character.isLetter(letter)) {
                    System.out.println("Saisie invalide ! Veuillez entrer une lettre entre A et " + maxLetter + ".");
                    continue;
                }
                int cardIndex = letterToIndex(letter);
                Animal animal = human.getHandCard(cardIndex);

                return cardIndex;

            }
            // ❌ Saisie invalide → on reboucle et on redemande
            System.out.println("Lettre incorrecte ! Votre main va de A à " + maxLetter + ".");
        }
    }

    private int letterToIndex(char letter) {
        // Character.toUpperCase pour accepter 'a' ou 'A'
        // On utilise l'ASCII pour trouver l'index  'A' = 65
        return 1+(Character.toUpperCase(letter) - 'A');
    }


    private int toIndex(int position) {
        return position - 1;
    }


    private boolean canAfford(Animal card, User human) {
        if (human.hasEnoughBones(card)) {
            this.displayMessage("Pas assez d'os (" + human.getBonesToken() + "/" + card.getBoneCost() + ").");
            return false;
        }
        if(human.hasEnoughBlood(card)){
            this.displayMessage("Pas assez de créatures à sacrifier pour poser cette carte, veuillez en choisir une autre coutant moins de "+ human.getSacrificableCount());
            return false;
        }
        return true;
    }

    // on pioche 1 carte dans le Deck et on l'ajoute à la main.
    public void draw(User joueur) {
        // Si le deck ET la main sont vides, pas la peine de demander
        if (joueur.getDeckSize() == 0 && joueur.getHandSize() == 0) {
            this.displayMessage("Vous n'avez plus de cartes ni en main ni dans la pioche... Il va falloir faire avec !");
            return;
        }
        // L'utilisateur a le choix
        boolean wantsToDraw = this.askConfirmation("Voulez-vous piocher une carte pour ce tour ?");

        if (wantsToDraw) {
            try {
                Animal drawnAnimalCard = joueur.draw();
                joueur.addCard(drawnAnimalCard);
                this.displayMessage("Vous avez pioché : " + drawnAnimalCard.getName());
            } catch (NoSuchElementException n) {
                this.displayMessage("Oups il n'y a plus de cartes dans la pioche...");
            }
        } else {
            this.displayMessage("Vous avez décidé de ne pas piocher... Étais-ce un bon choix...");
        }
    }




    // Affiche un message simple (dégâts, pioche,etc)
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayFinalResult(boolean victory){
        if (victory) {
            System.out.println("Félicitations ! Vous avez survécu à la forêt.");
        } else {
            System.out.println("L'obscurité vous englouti... Partie terminée.");
        }
    }


    public Animal offerNewCardChoice(Animal card1, Animal card2) {
        System.out.println("\n--- RÉCOMPENSE ---");
        System.out.println("Félicitations ! Vous avez survécu aux deux première parties ! Cela en sera t-il pour la dernière ? ");
        System.out.println("Pour vous aider à vaincre Leshy voici deux cartes, choisissez en UNE pour parfaire votre deck :");

        // Affichage des cartes pour aider
        displayCardOption(1, card1);
        displayCardOption(2, card2);

        while (true) {
            System.out.print("Votre choix (1 ou 2) : ");

            if (m_scanner.hasNextInt()) {
                int choice = m_scanner.nextInt();
                if (!Character.isDigit(choice)) {
                    System.out.println("Saisie invalide ! Veuillez entrer 1 ou 2.");
                    continue;
                }
                if (choice == 1) {
                    return card1;
                }
                if (choice == 2) {
                    return card2;
                }
            }
            System.out.println("Erreur : Choix inconnu. Veuillez saisir le chiffre 1 ou 2.");
        }
    }


    private void displayCardOption(int index, Animal card) {
        String labelCout;

        if (card.getBloodCost() > 0) {
            // Si la carte coûte du sang
            labelCout = card.getBloodCost() + " Sang";
        } else {
            // Sinon, on affiche le coût en os
            labelCout = card.getBoneCost() + " Os";
        }

        // ligne finale :
        System.out.println("  [" + index + "] " + card.getName() + " (Atk: " + card.getAttackPoints() + ", HP: " + card.getHealthPoints() + ", Coût: " + labelCout + ")");
    }



    //human.addPermanentCard();

           /*
        public boolean prepareSlot(int pos, int bloodAlreadyPaid, User human) {
            Optional<Card> optTarget = human.getCardAt(pos);

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
                human.removeCardAt(pos);
                bloodAlreadyPaid = 1;
                human.addBonesToken(); // Règle : tout sacrifice donne +1 os
                this.displayMessage("Sacrifice de " + target.getName() + " pour libérer la place (+1 sang).");
                this.displayMessage("Emplacement libéré. +1 os.");
                return true; // L'emplacement est maintenant libre, on renvoie true !
            }

            return false; // L'utilisateur a annulé, on ne peut pas poser
        }
         */


}
